package com.epam.gym.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.epam.gym.model.Trainer;
import jakarta.persistence.NonUniqueResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDAOImplTest {

    @InjectMocks
    private TrainerDAOImpl trainerDAO;
    @Mock
    private EntityManagerFactory emf;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Trainer> query;


    @BeforeEach
    public void setUp() {
        when(emf.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testFindByUserName() {
        String userName = "test_user";
        Trainer trainer = new Trainer();

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(eq("userName"), eq(userName))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainer);

        Trainer foundTrainer = trainerDAO.findByUserName(userName);
        assertNotNull(foundTrainer);
        assertSame(trainer, foundTrainer);
    }

    @Test
    public void testFindByUserName_NoResult() {
        String userName = "non_existing_user";

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(eq("userName"), eq(userName))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        assertThrows(RollbackException.class, () -> trainerDAO.findByUserName(userName));
    }

    @Test
    public void testFindByUserName_NonUniqueResult() {
        String userName = "duplicate_user";

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(eq("userName"), eq(userName))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NonUniqueResultException.class);

        assertThrows(RollbackException.class, () -> trainerDAO.findByUserName(userName));
    }

    @Test
    public void testGetFreeActiveTrainers() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        Collection<Trainer> freeActiveTrainers = trainerDAO.getFreeActiveTrainers();
        assertNotNull(freeActiveTrainers);
        assertEquals(trainers.size(), freeActiveTrainers.size());
    }

    @Test
    public void testGetAll() {
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        Collection<Trainer> allTrainers = trainerDAO.getAll();
        assertNotNull(allTrainers);
        assertEquals(trainers.size(), allTrainers.size());
    }

    @Test
    public void testAdd() {
        Trainer trainer = new Trainer();

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));

        assertDoesNotThrow(() -> trainerDAO.save(trainer));
    }

    @Test
    public void testUpdate() {
        Trainer trainer = new Trainer();

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));

        assertDoesNotThrow(() -> trainerDAO.update(trainer));
    }
}

