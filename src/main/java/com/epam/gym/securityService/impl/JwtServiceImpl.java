package com.epam.gym.securityService.impl;

import com.epam.gym.exception.InvalidJwtTokenException;
import com.epam.gym.securityService.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@PropertySource("classpath:secret.properties")
public class JwtServiceImpl implements JwtService {

    @Value("${secretKey}")
    private String SECRET_KEY;
    private static Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, String username) {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSighInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public void validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSighInKey())
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (Exception ex) {
            logger.error("Token is not valid: {}", ex.getMessage());
            throw new InvalidJwtTokenException("JWT token is invalid");
        }
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSighInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSighInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
