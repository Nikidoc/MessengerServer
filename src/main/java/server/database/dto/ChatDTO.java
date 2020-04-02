package server.database.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatDTO extends BaseDTO {
    private UserDTO creatorId;
    private String name;
    private MessageDTO lastMessage;
    private List<String> members;
    private List<MessageDTO> messages;
    private boolean group;
}
