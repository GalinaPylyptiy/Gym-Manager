package com.epam.gym.dao.impl;

import com.epam.gym.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserDAOImplTest {

    @InjectMocks
    private UserDAOImpl userDAO;
    @Mock
    private EntityManagerFactory emf;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private Query query;
    @Mock
    private TypedQuery<User> typedQuery;

    @BeforeEach
    public void setUp() {
        when(emf.createEntityManager()).thenReturn(entityManager);
    }
//
//    @Test
//    public void testFindById() {
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        when(entityManager.find(User.class, userId)).thenReturn(user);
//
//        Optional<User> foundUser = userDAO.findById(userId);
//
//        assertTrue(foundUser.isPresent());
//        assertEquals(userId, foundUser.get().getId());
//    }

    @Test
    public void testActivateProfile() {
        User user = new User();
        user.setId(1L);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("isActive", true)).thenReturn(query);
        when(query.setParameter("userId", user.getId())).thenReturn(query);

        userDAO.activate(user);

        verify(query, times(1)).executeUpdate();
    }

    @Test
    public void testDeactivateProfile() {
        User user = new User();
        user.setId(1L);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("isActive", false)).thenReturn(query);
        when(query.setParameter("userId", user.getId())).thenReturn(query);

        userDAO.deactivate(user);

        verify(query, times(1)).executeUpdate();

    }

    @Test
    public void testChangePassword() {
        User user = new User();
        user.setPassword("oldPassword".toCharArray());
        String newPassword = "newPassword";

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("newPassword", newPassword)).thenReturn(query);
        when(query.setParameter("oldPassword", user.getPassword())).thenReturn(query);

        userDAO.changePassword(user, newPassword);

        verify(query, times(1)).executeUpdate();
    }

    @Test
    public void testIsUserExists() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password".toCharArray());
        user.setActive(true);

        Long expectedResult = 1L;

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("username", user.getUserName())).thenReturn(query);
        when(query.setParameter("password", user.getPassword())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        boolean exists = userDAO.isUserExists(user);

        assertTrue(exists);
    }

    @Test
    public void testSerialNumberCount() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password".toCharArray());
        user.setActive(true);

        int expectedResult = 4;

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        int serialNumberCount = userDAO.getSerialNumberCount(user.getUserName());

        assertEquals(4, serialNumberCount);
    }
}
