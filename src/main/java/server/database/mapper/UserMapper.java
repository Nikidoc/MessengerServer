package server.database.mapper;


import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.database.dto.UserDTO;
import server.database.entity.User;
import server.database.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Data
//@NoArgsConstructor
public class UserMapper{

    private UserServiceImpl userService;
    private ModelMapper authUserMapper;
    private ModelMapper otherUserMapper;

    @Autowired
    public UserMapper(UserServiceImpl userService) {
        this.userService = userService;

        this.authUserMapper = initAuthUserMapper();
        this.otherUserMapper = initNoAuthUserMapper();
    }

    private ModelMapper initAuthUserMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapping -> mapping.skip(UserDTO::setPassword));
//                .setPostConverter(entityToDTOChatsConverter());

        mapper.createTypeMap(UserDTO.class, User.class);

        return mapper;
    }

    private ModelMapper initNoAuthUserMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(UserDTO::setPassword);
                    mapping.skip(UserDTO::setEmail);
                    mapping.skip(UserDTO::setId);
                    mapping.skip(UserDTO::setStatus);
                    mapping.skip(UserDTO::setUpdated);
                    mapping.skip(UserDTO::setChats);
                });

        mapper.createTypeMap(UserDTO.class, User.class);

        return mapper;
    }

    public List<UserDTO> mapNoAuthUserListEntityToDTO(List<User> users){
        List<UserDTO> dtoUsers = new ArrayList<>();
        for (User user : users) {
            dtoUsers.add(mapNoAuthUserEntityToDTO(user));
        }
        return dtoUsers;
    }

    public UserDTO mapAuthUserEntityToDTO(User user) {
        return Objects.isNull(user) ? null : authUserMapper.map(user, UserDTO.class);
    }

    public UserDTO mapNoAuthUserEntityToDTO(User user) {
        return Objects.isNull(user) ? null : otherUserMapper.map(user, UserDTO.class);
    }

    public User mapAuthUserDtoToEntity(UserDTO userDTO) {
        return Objects.isNull(userDTO) ? null : authUserMapper.map(userDTO, User.class);
    }

    public User mapNoAuthUserDtoToEntity(UserDTO userDTO) {
        return Objects.isNull(userDTO) ? null : otherUserMapper.map(userDTO, User.class);
    }

}
