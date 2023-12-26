package com.epam.gym.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.message.TrainingReportMessageSender;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainingReportMessageSender messageSender;



    private Training createTraining() {
        Training training = new Training();
        Trainee trainee = new Trainee();
        User traineeUser = new User();
        traineeUser.setUserName("Trainee.User");
        trainee.setUser(traineeUser);
        training.setTrainee(trainee);
        Trainer trainer = new Trainer();
        User trainerUser = new User();
        trainerUser.setUserName("Trainee.User");
        trainer.setUser(trainerUser);
        training.setTrainer(trainer);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(3L);
        training.setTrainingType(trainingType);
        training.setTrainingTime(new Date());
        training.setTrainingDuration(80);
        training.setTrainingName("usual training");
        return training;
    }

    @Test
    public void testGetAll() {

        Training training1 = new Training();
        Training training2 = new Training();

        when(trainingDAO.getAll()).thenReturn(List.of(training1, training2));

        Collection<Training> trainings = trainingService.getAll();

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
    }

    @Test
    public void testAdd() {

        Training newTraining = createTraining();

        TrainingReportRequest trainingReportRequest = new TrainingReportRequest();

        when(trainingMapper.toRequestDtoWithoutActionType(newTraining)).thenReturn(trainingReportRequest);

        doNothing().when(messageSender).send(trainingReportRequest);

        assertDoesNotThrow(() -> trainingService.save(newTraining));

    }
}


