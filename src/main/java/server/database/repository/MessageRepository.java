package server.database.repository;

import server.database.entity.Message;
import server.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAllByAuthor(Long authorId);
    public List<Message> findAllByChatOrderByCreated(Long chatId);
    public Message findFirstByChatOrderByCreatedDesc(Long chatId);
}
