package server.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.database.entity.Chat;
import server.database.entity.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<ChatDTO> chats;



//    private List<Chat> chats;
//    private List<Role> roles;

}
