package server.security.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.security.authentication.AuthException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TokenHandler {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long validityInMilliseconds;

    @Value("${jwt.token.header}")
    private String headerName;

    private static final String KEY_USERNAME = "username";

//    @PostConstruct
//    protected void init() {
//        secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }

    public String extractUsername(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body.getSubject();
    }

    public String createToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(KEY_USERNAME, username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
//        Date validity = new Date(now.getTime() + 3600000);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secret)//
//                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString("snejok".getBytes()))
                .compact();
    }

    public boolean validateToken(String token) throws AuthException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException("JWT token is expired or invalid");
        }
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(headerName);
    }

}
