package server.database.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.database.dto.ChatDTO;
import server.database.dto.MessageDTO;
import server.database.entity.Chat;
import server.database.entity.Message;
import server.database.entity.User;
import server.database.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Component
//@Data
//@NoArgsConstructor
public class MessageMapper{

    private UserServiceImpl userService;
    private UserMapper userMapper;
    private ModelMapper standardMapper;


    @Autowired
    public MessageMapper(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.standardMapper = initStandardMapper();
    }

    private ModelMapper initStandardMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(Message.class, MessageDTO.class)
                .setPostConverter(addAuthor());

        mapper.createTypeMap(MessageDTO.class, Message.class);

        return mapper;
    }

    public List<MessageDTO> mapStandardListEntityToDTO(List<Message> messages){
        List<MessageDTO> dtoMessages = new ArrayList<>();
        for (Message message : messages) {
            dtoMessages.add(mapStandardEntityToDTO(message));
        }
        return dtoMessages;
    }

    private Converter<Message, MessageDTO> addAuthor(){
        return context -> {
            MessageDTO destination = context.getDestination();
            User author = userService.findById(context.getSource().getAuthor());
            destination.setAuthor(userMapper.mapNoAuthUserEntityToDTO(author));
            return destination;
        };
    }


    public MessageDTO mapStandardEntityToDTO(Message message){
        return Objects.isNull(message) ? null : standardMapper.map(message, MessageDTO.class);
    }

    public Message mapStandardDtoToEntity(MessageDTO messageDTO){
        return Objects.isNull(messageDTO) ? null : standardMapper.map(messageDTO, Message.class);
    }

}
