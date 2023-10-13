package network.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "dialogue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dialogue implements Serializable {
    @Id
    private String id;
    private String dialogueId;
    private String fromUserId;
    private String toUserId;
    private String text;
    private LocalDateTime createDatetime;
}