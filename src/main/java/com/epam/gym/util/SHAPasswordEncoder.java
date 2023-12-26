package com.epam.gym.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class SHAPasswordEncoder implements PasswordEncoder {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 8;

    @Override
    public String encode(CharSequence rawPassword) {
        String salt = generateSalt();
        String saltedPassword = rawPassword + salt;
        String hashedPassword = hashPassword(saltedPassword);
        return salt + hashedPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String salt = encodedPassword.substring(0, SALT_LENGTH*2);
        String saltedPassword = rawPassword + salt;
        String hashedPassword = hashPassword(saltedPassword);
        return encodedPassword.equals(salt + hashedPassword);
    }

    private String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = md.digest(password.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        int hex = 16;
        for (byte aByte : bytes) {
            int unsignedByte = aByte & 0xFF;
            result.append(Integer.toString(unsignedByte + 0x100, hex).substring(1));
        }
        return result.toString();
    }

}
