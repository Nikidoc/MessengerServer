package server.database.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.entity.Message;
import server.database.entity.Status;
import server.database.repository.MessageRepository;
import server.database.service.MessageService;

import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message create(Message message) {
        message.setStatus(Status.ACTIVE);

        Message createdMessage = messageRepository.saveAndFlush(message);
        return createdMessage;
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getAllByAuthorId(Long id) {
        return messageRepository.findAllByAuthor(id);
    }

    @Override
    public List<Message> getAllByChatId(Long id) {
        return messageRepository.findAllByChatOrderByCreated(id);
    }

    @Override
    public Message findById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message getLastMessage(Long chatId) {
        return messageRepository.findFirstByChatOrderByCreatedDesc(chatId);
    }
}
