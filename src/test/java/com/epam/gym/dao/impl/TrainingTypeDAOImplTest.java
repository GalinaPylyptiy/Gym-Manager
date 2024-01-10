package com.epam.gym.dao.impl;

import com.epam.gym.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDAOImplTest {

    @InjectMocks
    private TrainingTypeDAOImpl trainingTypeDAO;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<TrainingType> query;

    @Test
    public void testFindById() {
        Long id = 1L;
       TrainingType trainingType = new TrainingType();

        when(entityManager.find(TrainingType.class, id)).thenReturn(trainingType);

        Optional<TrainingType> foundTrainingType = trainingTypeDAO.findById(id);
        assertTrue(foundTrainingType.isPresent());
        assertSame(trainingType, foundTrainingType.get());
    }

    @Test
    public void testFindById_NotFound() {
        Long id = 1L;

        when(entityManager.find(TrainingType.class, id)).thenReturn(null);

        Optional<TrainingType> foundTraining = trainingTypeDAO.findById(id);

        assertFalse(foundTraining.isPresent());
    }

    @Test
    public void testGetAll() {
        List<TrainingType> trainingTypes = Arrays.asList(new TrainingType(), new TrainingType());

        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingTypes);

        Collection<TrainingType> allTrainingTypes = trainingTypeDAO.getAll();
        assertNotNull(allTrainingTypes);
        assertEquals(trainingTypes.size(), allTrainingTypes.size());
    }
}