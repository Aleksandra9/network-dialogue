package network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewMessageModel {
  @JsonProperty("text")
  private String text;
}
