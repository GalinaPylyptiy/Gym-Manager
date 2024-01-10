package com.epam.gym.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
