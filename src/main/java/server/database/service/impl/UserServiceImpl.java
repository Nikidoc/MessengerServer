package server.database.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.database.entity.Chat;
import server.database.entity.Status;
import server.database.entity.User;

import server.database.entity.UserToChat;
import server.database.repository.ChatRepository;
import server.database.repository.UserToChatRepository;
import server.database.repository.UserRepository;
import server.database.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.security.SecurityUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserToChatRepository userToChatRepository;

    @Override
    public User register(User user) {
        user.setStatus(Status.ACTIVE);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User registeredUser = userRepository.saveAndFlush(user);
        log.trace("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.trace("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        if (result == null) {
            log.warn("IN findByLogin - user not found by login {}", username);
            return null;
        }
        log.trace("IN findByLogin - user {} found by login {}", result, username);
        return result;
    }

    @Override
    public List<User> findByUsernameLike(String username) {

        List<User> result = userRepository.findByUsernameLike("%" + username + "%");
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null){
            log.warn("IN findById - user not found by id: {}", id);
            return null;
        }
        log.trace("IN findById - user {} found by id", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.trace("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public boolean isEmailValid(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean isUsernameValid(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public List<Chat> findAllChatsByUserId(Long id) {
        List<UserToChat> userChats = userToChatRepository.findAllByUserId(id);

        List<Chat> chats = new ArrayList<>();
        Chat chat;

        for (UserToChat iterator : userChats) {
            chat = chatRepository.findById(iterator.getChatId()).orElse(null);
            if (Objects.isNull(chat)){
                log.trace("chat not download");
                continue;
            }
            chats.add(chat);
        }

        return chats;
    }

    @Override
    public List<User> findAllUsersByChatId(Long id) {
        List<UserToChat> userChats = userToChatRepository.findAllByChatId(id);

        List<User> users = new ArrayList<>();
        User user;

        for (UserToChat iterator : userChats) {
            user = userRepository.findById(iterator.getUserId()).orElse(null);
            if (Objects.isNull(user)){
                log.trace("user not download");
                continue;
            }
            if (getAuthUserId().equals(user.getId())){
                continue;
            }
            users.add(user);
        }

        return users;
    }

    @Override
    public Long getIdByUsername(String username) {
        User user = findByUsername(username);
        return user.getId();
    }

    @Override
    public String getAuthUsername(){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return securityUser.getUsername();
    }

    @Override
    public Long getAuthUserId(){
        return getIdByUsername(getAuthUsername());
    }
}
