package server.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO extends BaseDTO {

    private UserDTO author;
    private Long chat;
    private String text;

}
