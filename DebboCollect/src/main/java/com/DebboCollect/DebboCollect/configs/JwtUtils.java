package com.DebboCollect.DebboCollect.configs;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    private final long jwtExpirationMs =
            86400000;

    private Key key() {

        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes()
        );
    }

    public String generateJwtToken(
            String username
    ) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpirationMs
                        )
                )
                .signWith(
                        key(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String getUserNameFromJwtToken(
            String token
    ) {

        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(
            String authToken
    ) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(authToken);

            return true;

        } catch (JwtException e) {

            return false;
        }
    }
}