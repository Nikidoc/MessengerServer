package server.database.service;

import server.database.entity.Message;

import java.util.List;

public interface MessageService {
    public Message create(Message chat);
    public List<Message> getAll();
    public List<Message> getAllByAuthorId(Long id);
    public List<Message> getAllByChatId(Long id);
    public Message findById(Long id);
    public void delete(Long id);
    public Message getLastMessage(Long chatId);
}
