package server.database.service;

import server.database.dto.ChatDTO;
import server.database.entity.Chat;
import server.database.entity.User;

import java.util.List;

public interface ChatService {
    Chat create(Chat chat);
    Chat create(ChatDTO chat);
    List<Chat> getAll();
    List<Chat> getAllByUserId(Long id);
    Chat findById(Long id);
    void delete(Long id);
    Chat findPrivateChat(Long id1, Long id2);
    boolean chatExists(Long id);
    boolean checkAccess(Long chatId, Long userId);
    void addUserToChat(Long userId, Long chatId);


}
