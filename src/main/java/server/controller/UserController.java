package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.ChatDTO;
import server.database.dto.UserDTO;
import server.database.mapper.ChatMapper;
import server.database.mapper.UserMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping(path = "user")
public class UserController {

    private final ChatMapper chatMapper;

    private final UserServiceImpl userService;

    private final ChatServiceImpl chatService;

    private final UserMapper userMapper;

    public UserController(ChatMapper chatMapper, UserServiceImpl userService, ChatServiceImpl chatService, UserMapper userMapper) {
        this.chatMapper = chatMapper;
        this.userService = userService;
        this.chatService = chatService;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public ResponseEntity getUser(@RequestParam(name = "username", required = false) String username){

        String authUsername = userService.getAuthUsername();
        UserDTO userDTO;
        if (username == null || username.isEmpty() || authUsername.equals(username)){
            userDTO = userMapper.mapAuthUserEntityToDTO(userService.findByUsername(authUsername));
        } else {
            userDTO = userMapper.mapNoAuthUserEntityToDTO(userService.findByUsername(username));
        }


//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        var xhr = new XMLHttpRequest();
//        var count = 0;
//        console.time("label")
//        do {
//            var xhr = new XMLHttpRequest();
//            xhr.open('GET', "http://localhost:8090/user");
//            xhr.setRequestHeader('Content-Type', 'application/json');
//            xhr.setRequestHeader('X-Auth-Token', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOaWtpdG9za2FzIiwiaWF0IjoxNTgxNzE0NDIyLCJleHAiOjE1ODUzMTQ0MjJ9.QHKunc-1WCWQZobfSsH1CduSLTcaNbLWXGAEfWSklAM');
//            xhr.send();
//            count++;
//        } while (count != 1000 || xhr.status == 200);
//        console.log(xhr.status);
//        console.timeEnd("label");




//
//        var count = 0;
//        console.time("time")
//        do {
//            var xhr = new XMLHttpRequest();
//
//            xhr.open('GET', "http://localhost:8090/user", true);
//            xhr.setRequestHeader('Content-Type', 'application/json');
//            xhr.setRequestHeader('X-Auth-Token', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOaWtpdG9za2FzIiwiaWF0IjoxNTgxNzE0NDIyLCJleHAiOjE1ODUzMTQ0MjJ9.QHKunc-1WCWQZobfSsH1CduSLTcaNbLWXGAEfWSklAM');
//            xhr.onload = function(e){
//                if(xhr.readyState == 4){
//                    console.timeEnd("time");
//                    console.log(xhr);
//                    console.log(e);
//                }
//            };
//            var us = (count % 2 == 0) ? "Nikitoskas" : "test";
////             var us = count;
//            xhr.setRequestHeader("username", us);
//            xhr.send({"count":count});
//            count++;
//        } while (count != 301);
//        console.log(xhr.status);

        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping("test")
    public ResponseEntity test(){
        return ResponseEntity.ok(chatMapper.mapStandardChatMapperEntityToDto(chatService.findPrivateChat(6L, 2L)));
    }

    @GetMapping("chats")
    public ResponseEntity getChats(){
        Long id = userService.getAuthUserId();
        List<ChatDTO> chats = chatMapper.mapStandardListEntityToDTO(userService.findAllChatsByUserId(id));
        return ResponseEntity.ok(chats);
    }

    @GetMapping("users")
    public ResponseEntity getUsers(@RequestParam(name = "like", required = false) String username){
        if (username == null || username.equals("")){
           return ResponseEntity.ok(userMapper.mapNoAuthUserListEntityToDTO(userService.getAll()));
        }
        return ResponseEntity.ok(userMapper.mapNoAuthUserListEntityToDTO(userService.findByUsernameLike(username)));

    }



}
