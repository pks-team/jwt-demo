package com.pks.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Claims.EXPIRATION;
import static io.jsonwebtoken.Claims.SUBJECT;

/**
 * @author Martin Varga
 */
@Component
public class TokenService {

    private String secret;
    private Long expiration;

    @Autowired TokenService(@Value("${jwt.expiry}") Long expiration,
                            @Value("${jwt.secret}") String secret) {
        this.expiration = expiration;
        this.secret = secret;
    }

    public Map<String, Object> getInfoFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims;
    }

    public String generateToken(String userId, HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SUBJECT, userId);
        claims.put("remote-address", request.getRemoteAddr());
        claims.put("user-agent", request.getHeader("User-Agent"));
        Date exp = generateExpirationDate();
        claims.put(EXPIRATION, exp);
        claims.put("exp-human-readable", exp.toString());
        return this.generateToken(claims);
    }

    public void validateToken(String token) {
        getClaimsFromToken(token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
