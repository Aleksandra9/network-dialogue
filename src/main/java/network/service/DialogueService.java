package network.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import network.dto.DialogueMessage;
import network.dto.KafkaModel;
import network.dto.NewMessageModel;
import network.entity.Dialogue;
import network.enums.MessageStatus;
import network.repository.DialogueRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DialogueService {
    private final DialogueRepository dialogueRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${kafka.topic.out}")
    private String topicOut;

    public DialogueService(DialogueRepository dialogueRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.dialogueRepository = dialogueRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public List<DialogueMessage> getAllMessages(String userId, String friendId) {
        var list = dialogueRepository.getAllMessages(generateDialogId(userId, friendId));
        list.forEach(el -> {
            if (MessageStatus.NEW.name().equals(el.getStatus()) && el.getToUserId().equals(userId))
                sendMessage(userId, el.getId(), MessageStatus.READ);
        });
        return list.stream().map(DialogueMessage::new).collect(Collectors.toList());
    }

    public void createMessage(String userId, String friendId, NewMessageModel body) {
        var message = new Dialogue();
        message.setId(UUID.randomUUID().toString());
        message.setDialogueId(generateDialogId(userId, friendId));
        message.setStatus(MessageStatus.PENDING.name());
        message.setFromUserId(userId);
        message.setToUserId(friendId);
        message.setText(body.getText());
        message = dialogueRepository.saveAndFlush(message);
        sendMessage(friendId, message.getId(), MessageStatus.NEW);
    }

    private String generateDialogId(String userId, String friendId) {
        var dialogId = StringUtils.compare(userId, friendId) < 0 ? userId + friendId : friendId + userId;
        return UUID.nameUUIDFromBytes(dialogId.getBytes()).toString();
    }

    private void setStatus(String messageId, MessageStatus messageStatus) {
        dialogueRepository.setStatus(messageId, messageStatus.name());
    }

    @KafkaListener(topics = "#{'${kafka.topic.in}'}")
    public void getMessage(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                           @Header(KafkaHeaders.OFFSET) int offset) throws JsonProcessingException {
        log.info("Get new message partition [{}], offset [{}]", partition, offset);
        var info = (KafkaModel) objectMapper.readValue(message, KafkaModel.class);
        setStatus(info.getMessageId(), info.getStatus());
    }

    private void sendMessage(String userId, String messageId, MessageStatus messageStatus) {
        try {
            var info = new KafkaModel(userId, messageId, messageStatus);
            kafkaTemplate.send(topicOut, UUID.randomUUID().toString(), objectMapper.writeValueAsString(info));
        } catch (Exception e) {
            log.error("have problem", e);
        }

    }
}
