package com.epam.gym.dao.impl;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.epam.gym.model.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingDAOImplTest {

    @InjectMocks
    private TrainingDAOImpl trainingDAO;
    @Mock
    private EntityManagerFactory emf;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Training> criteriaQuery;
    @Mock
    private Root<Training> trainingRoot;
    @Mock
    private Join<Object, Object> firstJoin;
    @Mock
    private Join<Object, Object> secondJoin;
    @Mock
    private TypedQuery<Training> query;

    @BeforeEach
    public void setUp() {
        when(emf.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Training training = new Training();

        when(entityManager.find(Training.class, id)).thenReturn(training);

        Training foundTraining = trainingDAO.getById(id);
        assertNotNull(foundTraining);
        assertSame(training, foundTraining);
    }

    @Test
    public void testFindById_NotFound() {
        Long id = 1L;

        when(entityManager.find(Training.class, id)).thenReturn(null);

        Training foundTraining = trainingDAO.getById(id);
        assertNull(foundTraining);
    }

    @Test
    public void testGetAll() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());

        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        Collection<Training> allTrainings = trainingDAO.getAll();
        assertNotNull(allTrainings);
        assertEquals(trainings.size(), allTrainings.size());
    }

    @Test
    public void testAdd() {
        Training training = new Training();

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));

        assertDoesNotThrow(() -> trainingDAO.save(training));
    }

    @Test
    public void testUpdate() {
        Training training = new Training();

        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));

        assertDoesNotThrow(() -> trainingDAO.update(training));
    }

    @Test
    public void testGetTraineeTrainings() {
        String traineeUserName = "traineeUser";
        List<Training> trainings = Arrays.asList(new Training(), new Training());

        when(emf.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(trainingRoot);
        when(trainingRoot.join("trainee", JoinType.INNER)).thenReturn(firstJoin);
        when(firstJoin.join("user", JoinType.INNER)).thenReturn(secondJoin);
        when(criteriaQuery.where((Expression<Boolean>) any())).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        Collection<Training> traineeTrainings = trainingDAO.getTraineeTrainings(traineeUserName);

        assertNotNull(traineeTrainings);
        assertEquals(trainings.size(), traineeTrainings.size());
    }

    @Test
    public void testGetTrainerTrainings() {
        String trainerUserName = "trainerUser";
        List<Training> trainings = Arrays.asList(new Training(), new Training());

        when(emf.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(trainingRoot);
        when(trainingRoot.join("trainer", JoinType.INNER)).thenReturn(firstJoin);
        when(firstJoin.join("user", JoinType.INNER)).thenReturn(secondJoin);
        when(criteriaQuery.where((Expression<Boolean>) any())).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        Collection<Training> trainerTrainings = trainingDAO.getTrainerTrainings(trainerUserName);

        assertNotNull(trainerTrainings);
        assertEquals(trainings.size(), trainerTrainings.size());
    }
}

