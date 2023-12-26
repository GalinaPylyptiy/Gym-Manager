package com.epam.gym.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PasswordGeneratorTest {

    @Test
    public void testGenerateRandomPassword() {
        int passwordLength = 10;
        String generatedPassword = PasswordGenerator.generateRandomPassword(passwordLength);

        assertNotNull(generatedPassword);
        assertEquals(passwordLength, generatedPassword.length());

        for (char character : generatedPassword.toCharArray()) {
            boolean isValidCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                    .contains(String.valueOf(character));
            assertTrue(isValidCharacter);
        }
    }
}
