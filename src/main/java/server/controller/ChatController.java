package server.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.dto.ChatDTO;
import server.database.entity.Chat;
import server.database.mapper.ChatMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;


@RestController
@RequestMapping(path = "chat")
public class ChatController {

    private final ChatMapper chatMapper;

    private final ChatServiceImpl chatService;

    private final MessageServiceImpl messageService;

    private final UserServiceImpl userService;

    public ChatController(ChatMapper chatMapper, ChatServiceImpl chatService, MessageServiceImpl messageService, UserServiceImpl userService) {
        this.chatMapper = chatMapper;
        this.chatService = chatService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @RequestMapping(value = "{chatId}", method = RequestMethod.GET)
    public ResponseEntity getChat(@PathVariable String chatId){

        Long id;
        try{
            id = Long.parseLong(chatId);
        } catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Not valid path-format");
        }

        if (!isChatAccessedForAuthUser(id)){
            return ResponseEntity.badRequest().body("chat " + chatId + " not found or haven't access");
        }
        ChatDTO chat = chatMapper.mapStandardChatMapperEntityToDto(chatService.findById(id));
        return ResponseEntity.ok(chat);
    }

    @RequestMapping("messages")
    public ResponseEntity getAllMessages(@RequestParam(name = "chat_id") Long id){
        if (!isChatAccessedForAuthUser(id)){
            return ResponseEntity.badRequest().body("chat not found or haven't access");
        }
        return ResponseEntity.ok(messageService.getAllByChatId(id));
    }

    @PostMapping("create")
    public ResponseEntity createChat(@RequestBody ChatDTO chat){
        Chat newChat = chatService.create(chat);
        if (newChat == null){
            return new ResponseEntity("chat not saved", HttpStatus.FORBIDDEN);
        }
        ChatDTO newChatDTO = chatMapper.mapStandardChatMapperEntityToDto(chatService.findById(newChat.getId()));
        return ResponseEntity.ok(newChatDTO);
    }

    private boolean isChatAccessedForAuthUser(Long chatId){
        Long authUserId = userService.getAuthUserId();
        return chatService.checkAccess(chatId, authUserId);
    }

    @RequestMapping("test")
    public ResponseEntity test(@RequestBody ChatDTO chat){
        return ResponseEntity.ok(userService.findAllUsersByChatId(chat.getId()));
    }




}
