package network.service;

import lombok.extern.slf4j.Slf4j;
import network.dto.DialogueMessage;
import network.dto.NewMessageModel;
import network.repository.DialogueRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DialogueService {
    @Value("${network.url}")
    String networkUrl;
    private final DialogueRepository dialogueRepository;
    private final RestTemplate restTemplate;

    public DialogueService(DialogueRepository dialogueRepository, RestTemplate restTemplate) {
        this.dialogueRepository = dialogueRepository;
        this.restTemplate = restTemplate;
    }

    public List<DialogueMessage> getAllMessages(String userId, String friendId) {
        if (userEmpty(userId) || userEmpty(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return dialogueRepository.getAllMessages(generateDialogId(userId, friendId)).stream().map(DialogueMessage::new).collect(Collectors.toList());
    }

    public void createMessage(String userId, String friendId, NewMessageModel body) {
        if (!userEmpty(userId) || !userEmpty(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        dialogueRepository.createMessage(generateDialogId(userId, friendId), userId, friendId, body.getText());
    }

    private boolean userEmpty(String userId) {
        var status = restTemplate.getForEntity(networkUrl + "/user/get/" + userId, String.class).getStatusCodeValue();
        return status != 200;
    }

    private String generateDialogId(String userId, String friendId) {
        var dialogId = StringUtils.compare(userId, friendId) < 0 ? userId + friendId : friendId + userId;
        return UUID.nameUUIDFromBytes(dialogId.getBytes()).toString();
    }
}
