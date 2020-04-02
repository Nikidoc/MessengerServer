package server.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.repository.MessageRepository;
import server.database.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.service.impl.UserServiceImpl;
import server.security.service.TokenHandler;


@RestController
public class MainController {//no used class
    private final UserRepository userRepository;

    private final UserServiceImpl userService;

    private final MessageRepository messageRepository;

    private final TokenHandler tokenHandler;

    private final UserMapper userMapper;

    public MainController(UserRepository userRepository, UserServiceImpl userService, MessageRepository messageRepository, TokenHandler tokenHandler, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.tokenHandler = tokenHandler;
        this.userMapper = userMapper;
    }

    @RequestMapping("gettoken")
    public ResponseEntity getToken(@RequestParam(value = "login") String login){

        HashMap<Object, Object> response = new HashMap<>();
        String token = tokenHandler.createToken(login);
        response.put("login", login);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @RequestMapping("getusername")
    public ResponseEntity getUsername(@RequestHeader(value = "X-Auth-Token") String token){

        if (!tokenHandler.validateToken(token)){
            return ResponseEntity.badRequest().body("неверный токен");
        }
        String username = tokenHandler.extractUsername(token);
        HashMap<Object, Object> response = new HashMap<>();
        response.put("username", username);


        return ResponseEntity.ok(response);
    }

    @RequestMapping("getuser")
    public ResponseEntity getUser(@RequestHeader(value = "X-Auth-Token") String token){

        if (!tokenHandler.validateToken(token)){
            return ResponseEntity.badRequest().body("invalid token");
        }

        String username = tokenHandler.extractUsername(token);

        User user = userService.findByUsername(username);

        HashMap<Object, Object> response = new HashMap<>();
        response.put("user", userMapper.mapAuthUserEntityToDTO(user));


        return ResponseEntity.ok(response);
    }

//    @RequestMapping("messages")
//    public Iterable<Message> messages(@RequestParam(value = "id", defaultValue = "1") Long id){
//        return messageRepository.findAllByAuthor(userRepository.findById(id).get());
//    }

}
