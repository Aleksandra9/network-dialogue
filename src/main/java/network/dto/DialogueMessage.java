package network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import network.entity.Dialogue;

@Data
public class DialogueMessage {
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("text")
    private String text;

    public DialogueMessage(Dialogue dialogue) {
        this.from = dialogue.getFromUserId();
        this.to = dialogue.getToUserId();
        this.text = dialogue.getText();
    }
}
