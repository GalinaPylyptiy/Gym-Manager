package com.epam.gym.securityService;

import java.util.Map;

public interface JwtService {

    String extractUsername(String jwtToken);

    String generateToken(Map<String, Object> extraClaims, String  username);

    String generateToken(String username);

    void validateToken(String jwtToken);
}
