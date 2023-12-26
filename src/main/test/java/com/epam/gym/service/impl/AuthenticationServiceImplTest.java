//package com.epam.gym.service.impl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.epam.gym.dao.UserDAO;
//import com.epam.gym.exception.AuthenticationFailedException;
//import com.epam.gym.model.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static org.mockito.Mockito.*;
//
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthenticationServiceImplTest {
//
//    @InjectMocks
//    private AuthenticationServiceImpl authenticationService;
//
//    @Mock
//    private UserDAO userDAO;
//
//    @Test
//    public void testIsAuthenticatedSuccess() {
//
//        User user = new User();
//        user.setId(1L);
//        user.setUserName("Peter.Petrov");
//        user.setPassword("password".toCharArray());
//
//        when(userDAO.isUserExists(user)).thenReturn(true);
//
//        assertDoesNotThrow(() -> authenticationService.isAuthenticated());
//    }
//
//    @Test
//    public void testIsAuthenticatedFailure() {
//
//        User user = new User();
//        user.setId(1L);
//        user.setUserName("Peter.Petrov");
//        user.setPassword("password".toCharArray());
//
//        when(userDAO.isUserExists(user)).thenReturn(false);
//
//        assertThrows(AuthenticationFailedException.class, () -> authenticationService.isAuthenticated());
//    }
//}
