package server.security.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import server.database.entity.User;
import server.database.service.impl.UserServiceImpl;
import server.security.SecurityUser;
import server.security.SecurityUserFactory;




@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        SecurityUser securityUser = SecurityUserFactory.create(user);
//        securityUser.setPassword(new BCryptPasswordEncoder().encode("test"));
        return securityUser;
    }

}
