package server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import server.database.entity.Role;
import server.database.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUserFactory {

    public SecurityUserFactory() {
    }

    public static SecurityUser create(User user){
        return  SecurityUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
//                .authorities(mapToGrantedAuthorities(user.getRoles()))//insert authorities
                .authorities(null)//insert authorities
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    private static List<SecurityRole> mapToGrantedAuthorities(List<Role> roles){
        return roles.stream()
                .map(role -> new SecurityRole(role.getName()))
                .collect(Collectors.toList());
    }

}

