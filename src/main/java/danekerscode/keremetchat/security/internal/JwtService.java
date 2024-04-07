package danekerscode.keremetchat.security.internal;

import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.RoleType;
import danekerscode.keremetchat.model.enums.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class JwtService {
    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_TYPE_CLAIM = "tokenType";

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(User user, TokenType tokenType) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .claim(ROLE_CLAIM, user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.joining(",")))
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(UTF_8)))
                .compact();
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(UTF_8)))
                    .build().parseClaimsJws(jwt);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsernameFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(UTF_8)))
                .build().parseClaimsJws(jwt)
                .getBody().getSubject();
    }

    public String getRoleFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(UTF_8)))
                .build().parseClaimsJws(jwt)
                .getBody().get(ROLE_CLAIM, String.class);
    }
}