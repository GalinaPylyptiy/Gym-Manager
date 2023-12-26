package com.epam.gym.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dto.trainer.TrainerDTO;
import com.epam.gym.dto.trainer.TrainerProfileResponse;
import com.epam.gym.dto.trainer.TrainerRegisterRequest;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.dto.trainer.TrainerUpdateResponse;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.util.PasswordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TrainerMapperTest {
    @InjectMocks
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TraineeMapper traineeMapper;

    @Test
    public void testToModel() {
        TrainerRegisterRequest request = new TrainerRegisterRequest();
        request.setSpecializationId(1L);
        request.setFirstName("John");
        request.setLastName("Doe");

        TrainingType specialization = new TrainingType();
        specialization.setId(1L);

        when(trainingTypeService.findById(1L)).thenReturn(specialization);

        Trainer trainer = trainerMapper.toModel(request);

        assertEquals("John", trainer.getUser().getFirstName());
        assertEquals("Doe", trainer.getUser().getLastName());
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    public void testToDTO() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Smith");
        trainer.setUser(user);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Fitness");

        trainer.setSpecialization(specialization);

        TrainerDTO trainerDTO = trainerMapper.toDTO(trainer);

        assertEquals("Alice", trainerDTO.getFirstName());
        assertEquals("Smith", trainerDTO.getLastName());
        assertEquals("Fitness", trainerDTO.getSpecialization().getTrainingTypeName());
    }

    @Test
    public void testToProfileResponse() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setFirstName("Bob");
        trainer.getUser().setLastName("Johnson");
        trainer.getUser().setActive(false);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Yoga");
        trainer.setSpecialization(specialization);
        trainer.setTrainees(new ArrayList<>());

        TrainerProfileResponse response = trainerMapper.toProfileResponse(trainer);

        assertEquals("Bob", response.getFirstName());
        assertEquals("Johnson", response.getLastName());
        assertFalse(response.isActive());
        assertEquals("Yoga", response.getSpecialization().getTrainingTypeName());
        assertEquals(new ArrayList<>(), response.getTraineeList());
    }

    @Test
    public void testToUpdatedTrainer() {
        Trainer trainerToUpdate = new Trainer();
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        trainerToUpdate.setUser(user);

        TrainerUpdateRequest updateRequest = new TrainerUpdateRequest();
        updateRequest.setFirstName("NewFirstName");
        updateRequest.setLastName("NewLastName");
        updateRequest.setUsername("NewUsername");
        updateRequest.setPassword("NewPassword");
        updateRequest.setActive(true);

        Trainer updatedTrainer = trainerMapper.toUpdatedTrainer(trainerToUpdate, updateRequest);

        assertEquals("NewFirstName", updatedTrainer.getUser().getFirstName());
        assertEquals("NewLastName", updatedTrainer.getUser().getLastName());
        assertEquals("NewUsername", updatedTrainer.getUser().getUserName());
        assertEquals("NewPassword", PasswordMapper.toString(updatedTrainer.getUser().getPassword()));
        assertTrue(updatedTrainer.getUser().isActive());
    }

    @Test
    public void testToUpdatedTrainerResponse() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setUserName("Alice");
        trainer.getUser().setFirstName("Alice");
        trainer.getUser().setLastName("Smith");
        trainer.getUser().setActive(false);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Yoga");

        trainer.setSpecialization(specialization);
        trainer.setTrainees(new ArrayList<>());

        TrainerUpdateResponse response = trainerMapper.toUpdatedTrainerResponse(trainer);

        assertEquals("Alice", response.getUsername());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertFalse(response.isActive());
        assertEquals("Yoga", response.getSpecialization().getTrainingTypeName());
        assertEquals(new ArrayList<>(), response.getTrainees());
    }

    @Test
    public void testToDtoList() {
        Trainer trainer1 = new Trainer();
        trainer1.setUser(new User());
        trainer1.getUser().setFirstName("Trainer1");

        Trainer trainer2 = new Trainer();
        trainer2.setUser(new User());
        trainer2.getUser().setFirstName("Trainer2");

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);

        List<TrainerDTO> trainerDTOs = trainerMapper.toDtoList(trainers);

        assertEquals(2, trainerDTOs.size());
        assertEquals("Trainer1", trainerDTOs.get(0).getFirstName());
        assertEquals("Trainer2", trainerDTOs.get(1).getFirstName());
    }

}