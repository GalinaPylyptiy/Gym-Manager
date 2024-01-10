package com.epam.gym.mapper;

import com.epam.gym.dto.training.TrainingCreateDTO;
import com.epam.gym.dto.training.TrainingDTO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingMapperTest {

    @InjectMocks
    private TrainingMapper trainingMapper;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;


    @Test
    public void testToDto() {
        Training training = new Training();
        training.setTrainingName("Training 101");
        training.setTrainingDuration(60);
        training.setTrainingTime(new Date());

        Trainer trainer = new Trainer();
        User trainerUser = new User();
        trainerUser.setUserName("trainer1");
        trainer.setUser(trainerUser);
        training.setTrainer(trainer);

        Trainee trainee = new Trainee();
        User traineeUser = new User();
        traineeUser.setUserName("trainee1");
        trainee.setUser(traineeUser);
        training.setTrainee(trainee);

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Fitness");
        training.setTrainingType(trainingType);

        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        assertEquals("Training 101", trainingDTO.getTrainingName());
        assertEquals(60, trainingDTO.getDuration());
        assertEquals("trainer1", trainingDTO.getTrainerName());
        assertEquals("trainee1", trainingDTO.getTraineeName());
        assertEquals("Fitness", trainingDTO.getType().getTrainingTypeName());
    }

    @Test
    public void testToModel() {
        TrainingCreateDTO createDTO = new TrainingCreateDTO();
        createDTO.setTrainingName("Training 102");
        createDTO.setDuration(45);
        createDTO.setTrainingDate(new Date());
        createDTO.setTrainerUsername("trainer2");
        createDTO.setTraineeUsername("trainee2");

        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setUserName("trainer2");

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUserName("trainee2");

        TrainingType trainingType = new TrainingType();
        when(trainerService.getByUserName("trainer2")).thenReturn(trainer);
        when(traineeService.getByUserName("trainee2")).thenReturn(trainee);

        Training training = trainingMapper.toModel(createDTO);
        training.setTrainingType(trainingType);

        assertEquals("Training 102", training.getTrainingName());
        assertEquals(45, training.getTrainingDuration());
        assertEquals("trainer2", training.getTrainer().getUser().getUserName());
        assertEquals("trainee2", training.getTrainee().getUser().getUserName());
        assertEquals(trainingType, training.getTrainingType());
    }

    @Test
    public void testToDtoList() {
        Training training1 = new Training();
        training1.setTrainingName("Training 101");
        training1.setTrainingDuration(45);
        training1.setTrainingTime(new Date());
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setUserName("trainer2");
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUserName("trainee2");
        training1.setTrainer(trainer);
        training1.setTrainee(trainee);
        training1.setTrainingType(new TrainingType());



        Training training2 = new Training();
        training2.setTrainingName("Training 102");
        training2.setTrainingDuration(50);
        training2.setTrainingTime(new Date());
        Trainer trainer2 = new Trainer();
        trainer2.setUser(new User());
        trainer2.getUser().setUserName("trainer3");
        Trainee trainee2 = new Trainee();
        trainee2.setUser(new User());
        trainee2.getUser().setUserName("trainee3");
        training2.setTrainer(trainer2);
        training2.setTrainee(trainee2);
        training2.setTrainingType(new TrainingType());

        List<Training> trainingList = new ArrayList<>();
        trainingList.add(training1);
        trainingList.add(training2);

        List<TrainingDTO> trainingDTOList = trainingMapper.toDtoList(trainingList);

        assertEquals(2, trainingDTOList.size());
        assertEquals("Training 101", trainingDTOList.get(0).getTrainingName());
        assertEquals("Training 102", trainingDTOList.get(1).getTrainingName());
    }
}
