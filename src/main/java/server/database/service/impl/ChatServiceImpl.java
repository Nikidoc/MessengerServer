package server.database.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.dto.ChatDTO;
import server.database.entity.Chat;
import server.database.entity.Status;
import server.database.entity.UserToChat;
import server.database.repository.ChatRepository;
import server.database.repository.UserToChatRepository;
import server.database.service.ChatService;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserServiceImpl userService;

    private final ChatRepository chatRepository;

    private final UserToChatRepository userToChatRepository;

    public ChatServiceImpl(ChatRepository chatRepository, UserToChatRepository userToChatRepository) {
        this.chatRepository = chatRepository;
        this.userToChatRepository = userToChatRepository;
    }

    @Override
    public Chat create(Chat chat) {

        chat.setStatus(Status.ACTIVE);

        return chatRepository.saveAndFlush(chat);
    }

    @Override
    public Chat create(ChatDTO chat) {

        List<String> users;
        Set<Long> usersId = new TreeSet<>();
        String chatName;

        users = chat.getMembers();
        chatName = chat.getName();

        Long authUserId = userService.getAuthUserId();



        for (Object entry : users) {
            String username = (String) entry;
            if (!userService.isUsernameValid(username)) {
                return null;
            }
            usersId.add(userService.getIdByUsername(username));
        }

        boolean group = !(usersId.size() == 1);



        Chat newChat = new Chat();
        newChat.setGroup(group);
        newChat.setCreatorId(authUserId);
        newChat.setName(chatName);
        newChat = create(newChat);

        Long chatId = newChat.getId();

        addUserToChat(authUserId, chatId);
        for (Long id: usersId) {
            addUserToChat(id, chatId);
        }

        return newChat;
    }

    @Override
    public List<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public List<Chat> getAllByUserId(Long id) {

        return chatRepository.findAllByCreatorIdEquals(id);
    }

    @Override
    public Chat findById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        chatRepository.deleteById(id);
    }

    @Override
    public Chat findPrivateChat(Long id1, Long id2) {
        Long chatId = chatRepository.getPrivateChatIdByUsers(id1, id2);
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public boolean chatExists(Long id) {
        return chatRepository.existsChatById(id);
    }

    @Override
    public boolean checkAccess(Long chatId, Long userId) {
        return userToChatRepository.existsUserChatsByChatIdAndUserId(chatId, userId);
    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {
        UserToChat userToChat = new UserToChat();
        userToChat.setChatId(chatId);
        userToChat.setUserId(userId);
        userToChat.setStatus(Status.ACTIVE);
        userToChatRepository.saveAndFlush(userToChat);
    }
}
