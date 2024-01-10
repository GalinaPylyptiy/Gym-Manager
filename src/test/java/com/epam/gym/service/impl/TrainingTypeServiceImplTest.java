package com.epam.gym.service.impl;

import com.epam.gym.dao.TrainingTypeDAO;
import com.epam.gym.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceImplTest {

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;



    @Test
    public void testGetById() {
        Long id = 1L;
        TrainingType trainingType = new TrainingType();

        when(trainingTypeDAO.findById(id)).thenReturn(Optional.of(trainingType));

        TrainingType foundTrainingType = trainingTypeService.findById(id);

        assertNotNull(foundTrainingType);
        assertEquals(trainingType, foundTrainingType);
    }

    @Test
    public void testGetAll() {

        TrainingType trainingType1 = new TrainingType();
        TrainingType trainingType2 = new TrainingType();

        when(trainingTypeDAO.getAll()).thenReturn(List.of(trainingType1, trainingType2));

        Collection<TrainingType> trainingTypes = trainingTypeService.getAll();

        assertNotNull(trainingTypes);
        assertEquals(2, trainingTypes.size());
    }


}