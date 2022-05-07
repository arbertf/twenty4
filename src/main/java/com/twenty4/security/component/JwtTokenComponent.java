package com.twenty4.security.component;

import com.twenty4.entity.User;
import com.twenty4.repository.UserRepository;
import com.twenty4.security.model.JwtUser;
import com.twenty4.security.model.ApplicationUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenComponent {

    static final String CLAIM_KEY_NAME = "firstName";

    static final String CLAIM_KEY_SURNAME = "lastName";

    static final String CLAIM_KEY_ID = "id";

    static final String CLAIM_KEY_ROLE = "role";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private final UserRepository userRepository;

    public JwtTokenComponent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        ApplicationUserDetails user = (ApplicationUserDetails) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(String.valueOf(claims.get(CLAIM_KEY_ID)));
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get(CLAIM_KEY_ROLE).toString();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token) .getBody();
        return claimsResolver.apply(claims);
    }

    public JwtUser validate(String token) {
        JwtUser user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new JwtUser();
            user.setId(Long.valueOf((String)body.get("id")));
            user.setEmail((String)body.get("email"));
            user.setRole((String)body.get("role"));
        }catch(Exception e) {}
        return user;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(CLAIM_KEY_ID, user.getId());
        claims.put(CLAIM_KEY_NAME, user.getFirstName());
        claims.put(CLAIM_KEY_SURNAME, user.getLastName());

        claims.put(CLAIM_KEY_ROLE, user.getRole().getName());

        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}

