package server.security;

import org.springframework.security.core.GrantedAuthority;

public class  SecurityRole implements GrantedAuthority {

    private String name;

    public SecurityRole() {
    }

    public SecurityRole(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
