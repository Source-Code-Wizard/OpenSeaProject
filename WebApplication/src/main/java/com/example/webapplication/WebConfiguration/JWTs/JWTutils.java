package com.example.webapplication.WebConfiguration.JWTs;

import com.example.webapplication.WebConfiguration.AuthenticatedUser;
import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import static com.example.webapplication.WebConfiguration.SecurityConstants.*;
import java.util.Date;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@Component
public class JWTutils {
    private static final Logger logger = LoggerFactory.getLogger(JWTutils.class);
  
    
    public String generateJwtToken(AuthenticatedUser userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }
    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
