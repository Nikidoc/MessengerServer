package server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.UserDTO;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.service.impl.UserServiceImpl;
import server.security.service.TokenHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

@PropertySource("application.properties")
@RestController
@Controller
@RequestMapping("auth")
public class AuthController {

    private final TokenHandler tokenHandler;

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @Value("${jwt.token.header}")
    private String tokenHeaderName;

    public AuthController(TokenHandler tokenHandler, UserServiceImpl userService, UserMapper userMapper) {
        this.tokenHandler = tokenHandler;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public ResponseEntity getAuthId(){
        return ResponseEntity.ok(userService.getAuthUserId());
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity sendToken(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @RequestBody(required = true) UserDTO userDTO
//            @RequestHeader(value = "X-Auth-Token")
    ) {
        String username;
        String password;

        if (userDTO.getUsername() != null && userDTO.getPassword() != null) {
            username = userDTO.getUsername();
            password = userDTO.getPassword();
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("no login or/and password");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("user is not found");
        }

        String token = tokenHandler.createToken(username);
        response.setHeader(tokenHeaderName, token);
        Map<String, Object> bodyResponse = new TreeMap<>();
        bodyResponse.put("message", "token for username: " + username + " was created");
        bodyResponse.put("token", token);

        return ResponseEntity.ok().body(bodyResponse);

    }

    @RequestMapping(value = "registration", method = RequestMethod.POST)
    public ResponseEntity register(
            @RequestBody(required = true) UserDTO userDTO
    ){
        String username;
        String password;
        if (userDTO != null && userDTO.getUsername() != null && userDTO.getPassword() != null){
            username = userDTO.getUsername();
            password = userDTO.getPassword();
        } else {
            return ResponseEntity.badRequest().body("no login and password or bad json");
        }

        if (userService.isEmailValid(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email: " + userDTO.getEmail() + " already registered");
        }

        if (userService.isUsernameValid(userDTO.getUsername())){
            return ResponseEntity.badRequest().body("Username: " + userDTO.getUsername() + " already registered");
        }

        User user = userMapper.mapAuthUserDtoToEntity(userDTO);
        User registeredUser = userService.register(user);


        return ResponseEntity.ok(userMapper.mapAuthUserEntityToDTO(registeredUser));
    }


}
