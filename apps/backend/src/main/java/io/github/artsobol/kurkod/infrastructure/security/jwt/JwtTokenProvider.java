package io.github.artsobol.kurkod.infrastructure.security.jwt;

import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.infrastructure.security.jwt.constants.JWTConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final Duration accessTokenLifetime;

    public JwtTokenProvider(@Value("${app.security.jwt.base64-signing-key}") String signingKey,
                            @Value("${app.security.jwt.access-token-lifetime}") Duration accessTokenLifetime) {
        this.secretKey = getKey(signingKey);
        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        this.accessTokenLifetime = accessTokenLifetime;
    }

    public String generateToken(@NonNull User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTConstants.USER_ID, user.getId());
        claims.put(JWTConstants.USERNAME, user.getUsername());
        claims.put(JWTConstants.USER_EMAIL, user.getEmail());
        claims.put(JWTConstants.USER_REGISTRATION_STATUS, user.getRegistrationStatus().name());
        claims.put(JWTConstants.LAST_UPDATE, LocalDateTime.now().toString());

        List<String> rolesList = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        claims.put(JWTConstants.ROLE, rolesList);

        return createToken(claims, user.getEmail());
    }

    public String refreshToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return createToken(claims, claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = jwtParser.parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(JWTConstants.USERNAME, String.class);
    }

    public String getUserId(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get(JWTConstants.USER_ID));
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get(JWTConstants.ROLE, List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getKey(String base64SigningKey) {
        byte[] decode64 = Decoders.BASE64.decode(base64SigningKey);
        return Keys.hmacShaKeyFor(decode64);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenLifetime.toMillis()))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }
}
