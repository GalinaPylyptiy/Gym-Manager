package com.epam.gym.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dao.UserDAO;
import com.epam.gym.model.User;
import com.epam.gym.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDAO userDAO;

    @Test
    public void testSetUsername_UniqueUsername() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");

        when(userDAO.isUserNameExists("John.Smith")).thenReturn(false);

        userService.setUsername(user);

        assertEquals("John.Smith", user.getUserName());
    }

    @Test
    public void testSetUsername_ExistingUsername() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");

        when(userDAO.isUserNameExists("John.Smith")).thenReturn(true);
        when(userDAO.getSerialNumberCount("John.Smith")).thenReturn(1);

        userService.setUsername(user);

        assertEquals("John.Smith_2", user.getUserName());
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword(PasswordGenerator.generateRandomPassword(10).toCharArray());

        userService.setPassword(user);

        assertNotNull(user.getPassword());
        assertEquals(10, user.getPassword().length);
    }

    @Test
    public void testChangePassword() {
        User user = new User();
        String newPassword = "newPassword";

        userDAO.changePassword(user, newPassword);

        verify(userDAO, times(1)).changePassword(user, newPassword);
    }

    @Test
    public void testActivateProfile() {
        User user = new User();

        userDAO.activate(user);

        verify(userDAO, times(1)).activate(user);
    }

    @Test
    public void testDeActivateProfile() {
        User user = new User();

        userDAO.activate(user);

        verify(userDAO, times(1)).activate(user);
    }
}
