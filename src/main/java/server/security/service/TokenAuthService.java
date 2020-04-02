package server.security.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import server.security.SecurityUser;
import server.security.authentication.AuthException;
import server.security.authentication.UserAuthentication;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenAuthService {
    @Value("${jwt.token.header}")
    private String headerName;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;


    public Authentication getAuthentication(@NonNull HttpServletRequest request) {
        String token = tokenHandler.resolveToken(request);
        try {
            if (token == null || !tokenHandler.validateToken(token)) {
                return null;
            }
        } catch (AuthException e){
            return null;
        }
        String userName = tokenHandler.extractUsername(token);
        UserDetails user = securityUserDetailsService.loadUserByUsername(userName);
        return new UserAuthentication((SecurityUser) user);

    }
}
