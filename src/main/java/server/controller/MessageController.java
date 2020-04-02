package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.database.entity.Chat;
import server.database.entity.Message;
import server.database.mapper.MessageMapper;
import server.database.mapper.UserMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(path = "message")
public class MessageController {

    private final MessageServiceImpl messageService;

    private final ChatServiceImpl chatService;

    private final UserServiceImpl userService;

    private final MessageMapper messageMapper;

    private final UserMapper userMapper;

    public MessageController(MessageServiceImpl messageService, ChatServiceImpl chatService, UserServiceImpl userService, MessageMapper messageMapper, UserMapper userMapper) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    @RequestMapping("sending")
    public ResponseEntity send(@RequestBody Message body){

        if (body == null){
            return ResponseEntity.badRequest().body("body not found");
        }

        if (body.getText() == null || body.getChat() == null){
            return ResponseEntity.badRequest().body("required data not found");
        }

        Long authUserId = userService.getAuthUserId();
        if (!chatService.checkAccess(body.getChat(), authUserId)){
            return ResponseEntity.badRequest().body("chat not found or haven't access");
        }

        body.setAuthor(authUserId);
        messageService.create(body);

        return ResponseEntity.ok(HttpServletResponse.SC_OK);
    }

    @RequestMapping("sending-to-user")
    public ResponseEntity sendToUser(@RequestBody Map<String, String> body){
        if (body == null){
            return ResponseEntity.badRequest().body("body not found");
        }

        String text = body.get("text");
        String receiverUsername = body.get("receiver_login");

        if (text == null || receiverUsername == null){
            return ResponseEntity.badRequest().body("required data (text) not found");
        }

        Long receiverId = userService.getIdByUsername(receiverUsername);
        Long authUserId = userService.getAuthUserId();
        if (!userService.isUsernameValid(receiverUsername)){
            return ResponseEntity.badRequest().body("user not registered");
        }

        Chat chat = chatService.findPrivateChat(authUserId, receiverId);
        Long chatId;
        if (chat == null){
            chat = new Chat();
            chat.setCreatorId(authUserId);
            chat.setGroup(false);
            chat = chatService.create(chat);
            chatId = chat.getId();
            chatService.addUserToChat(authUserId, chatId);
            chatService.addUserToChat(receiverId, chatId);
        } else {
            chatId = chat.getId();
        }

        Message message = new Message();
        message.setChat(chatId);
        message.setText(text);
        message.setAuthor(authUserId);
        messageService.create(message);

        return ResponseEntity.ok(HttpStatus.OK);

    }


}
