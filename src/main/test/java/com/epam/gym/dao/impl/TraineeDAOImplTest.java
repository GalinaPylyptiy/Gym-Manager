package com.epam.gym.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.epam.gym.dao.TraineeDAO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.User;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.PasswordMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeDAOImplTest {


    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    @Mock
    private EntityManagerFactory emf;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @BeforeEach
    public void setUp() {
       ReflectionTestUtils.setField(traineeDAO, "emf", emf);
    }

    @Test
    public void testFindByUserName() {
        // Arrange
        String testUserName = "testUser";
        User user = new User();
        user.setUserName(testUserName);
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setUser(user);

        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        when(query.setParameter("userName", testUserName)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedTrainee);

        // Act
        Trainee result = traineeDAO.findByUserName(testUserName);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getUser().getUserName());
    }

    @Test
    public void testFindByUserName_NoResultException() {
        // Arrange
        String testUserName = "testUser";
        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        when(query.setParameter("userName", testUserName)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        // Act and Assert
        assertThrows(RollbackException.class, () -> traineeDAO.findByUserName(testUserName));
    }

    @Test
    public void testFindByUsernameAndPassword() {

        String username = "testUser";
        char[] password = PasswordGenerator.generateRandomPassword(10).toCharArray();
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setUser(user);

        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        when(query.setParameter("username", username)).thenReturn(query);
        when(query.setParameter("password", PasswordMapper.toString(password))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedTrainee);

        Trainee result = traineeDAO.findByUsernameAndPassword(username, PasswordMapper.toString(password));

        assertNotNull(result);
        assertEquals(username, result.getUser().getUserName());
    }

    @Test
    public void testFindByUsernameAndPassword_NoResultException() {
        String username = "testUser";
        String password = "testPassword";
        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        when(query.setParameter("username", username)).thenReturn(query);
        when(query.setParameter("password", password)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        assertThrows(RollbackException.class, () -> traineeDAO.findByUsernameAndPassword(username, password));
    }

    @Test
    public void testSave() {
        // Arrange
        Trainee trainee = new Trainee();
        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        // Act
        assertDoesNotThrow(() -> traineeDAO.save(trainee));
    }

    @Test
    public void testDeleteByUserName() {
        // Arrange
        String username = "testUser";
        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        when(query.setParameter("userName", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Trainee());

        // Act
        assertDoesNotThrow(() -> traineeDAO.deleteByUserName(username));
    }

    @Test
    public void testUpdate() {
        // Arrange
        Trainee newTraineeInfo = new Trainee();
        when(emf.createEntityManager()).thenReturn(entityManager);

        when(entityManager.getTransaction()).thenReturn(transaction);

        // Act
        assertDoesNotThrow(() -> traineeDAO.update(newTraineeInfo));
    }

    @Test
    public void testGetAll() {
        when(emf.createEntityManager()).thenReturn(entityManager);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(any(), any())).thenReturn(query);

        Collection<Trainee> traineeList = traineeDAO.getAll();
        assertNotNull(traineeList);
    }
}
