package network.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.enums.MessageStatus;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class KafkaModel {
    private String userId;
    private String messageId;
    private MessageStatus status;
}
