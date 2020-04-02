package server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.database.entity.UserToChat;

import java.util.List;

public interface UserToChatRepository extends JpaRepository<UserToChat, Long> {
    List<UserToChat> findAllByChatId(Long id);
    List<UserToChat> findAllByUserId(Long id);
    boolean existsUserChatsByChatIdAndUserId(Long chatId, Long userId);
}
