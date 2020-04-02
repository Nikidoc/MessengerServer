import org.springframework.beans.factory.annotation.Autowired;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.service.impl.UserServiceImpl;

import java.util.List;

public class Test {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    @org.junit.jupiter.api.Test
    public void test1(){
//        User user = new User();

//        User user = userService.findByUsername("test");
        List<User> users = userService.findByUsernameLike("s");
//        user.setUsername("nikita");
//        user.setPassword("1111");
//        user.setEmail("4444");
        System.out.println();
    }
}
