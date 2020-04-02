package server.database.repository;

import org.springframework.data.jpa.repository.Query;
import server.database.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByCreatorIdEquals(Long id);

    @Query(value = "SELECT c.id FROM mydb.user_chats a, mydb.chats c where (a.user_id = ?1) and a.chat_id in (SELECT b.chat_id FROM mydb.user_chats b where b.user_id = ?2) and (c.group = false) and (a.chat_id = c.id)", nativeQuery = true)
    Long getPrivateChatIdByUsers(Long id1, Long id2);

    Boolean existsChatById(Long id);

}
