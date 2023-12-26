package com.epam.gym.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserNameGeneratorTest {

    @Test
    public void testGenerateUserName() {
        String firstName = "John";
        String lastName = "Doe";

        String generatedUserName = UserNameGenerator.generateUserName(firstName, lastName);

        assertNotNull(generatedUserName);

        String expectedUserName = firstName + "." + lastName;
        assertEquals(expectedUserName, generatedUserName);
    }
}
