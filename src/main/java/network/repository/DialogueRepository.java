package network.repository;

import network.entity.Dialogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DialogueRepository extends JpaRepository<Dialogue, String> {
    @Query(value = "select * " +
            "from dialogue " +
            "where dialogue_id =:dialogId " +
            "order by create_datetime desc", nativeQuery = true)
    List<Dialogue> getAllMessages(@Param("dialogId") String dialogId);

    @Modifying
    @Transactional
    @Query(value = "insert into dialogue (dialogue_id, from_user_id, to_user_id, text, create_datetime) " +
            "values (:dialogueId, :fromUserId, :toUserId, :text, now())", nativeQuery = true)
    void createMessage(@Param("dialogueId") String dialogueId, @Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId, @Param("text") String text);
}
