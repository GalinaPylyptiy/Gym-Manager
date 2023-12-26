package com.epam.gym.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dto.trainee.TraineeDTO;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.dto.trainee.TraineeRegisterRequest;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.User;
import com.epam.gym.util.PasswordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeMapperTest {

    @InjectMocks
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Test
    public void testToModel() {
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        TraineeRegisterRequest registerRequest = new TraineeRegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setDateOfBirth(dateOfBirth);
        registerRequest.setAddress("123 Main St");

        Trainee trainee = traineeMapper.toModel(registerRequest);

        assertEquals("John", trainee.getUser().getFirstName());
        assertEquals("Doe", trainee.getUser().getLastName());
        assertEquals(dateOfBirth, trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());
    }

    @Test
    public void testToUpdatedTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        TraineeUpdateRequest updateRequest = new TraineeUpdateRequest();
        updateRequest.setFirstName("Alice");
        updateRequest.setLastName("Smith");
        updateRequest.setUsername("alice123");
        updateRequest.setPassword("newPassword");
        updateRequest.setActive(true);
        updateRequest.setAddress("456 Elm St");
        updateRequest.setDateOfBirth(dateOfBirth);

        Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, updateRequest);

        assertEquals("Alice", updatedTrainee.getUser().getFirstName());
        assertEquals("Smith", updatedTrainee.getUser().getLastName());
        assertEquals("alice123", updatedTrainee.getUser().getUserName());
        assertEquals("newPassword", PasswordMapper.toString(updatedTrainee.getUser().getPassword()));
        assertTrue(updatedTrainee.getUser().isActive());
        assertEquals("456 Elm St", updatedTrainee.getAddress());
        assertEquals(dateOfBirth, updatedTrainee.getDateOfBirth());
    }

    @Test
    public void testToDTO() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Emily");
        user.setLastName("Johnson");
        trainee.setUser(user);

        TraineeDTO traineeDTO = traineeMapper.toDTO(trainee);

        assertEquals("Emily", traineeDTO.getFirstName());
        assertEquals("Johnson", traineeDTO.getLastName());
    }


    @Test
    public void testToProfileResponse() {
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setFirstName("Alice");
        trainee.getUser().setLastName("Smith");
        trainee.getUser().setActive(true);
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("456 Elm St");

        TraineeProfileResponse response = traineeMapper.toProfileResponse(trainee);

        assertEquals("Alice", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertTrue(response.isActive());
        assertEquals(dateOfBirth, response.getDateOfBirth());
        assertEquals("456 Elm St", response.getAddress());
        assertEquals(Collections.emptyList(), response.getTrainersList());
    }

    @Test
    public void testToUpdatedTraineeResponse() {
        Trainee trainee = new Trainee();
        User user  = new User();
        user.setUserName("Bob.Johnson");
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        user.setActive(false);
        trainee.setUser(user);
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("789 Oak St");
        trainee.setTrainers(Arrays.asList(new Trainer(), new Trainer()));

        TraineeUpdateResponse response = traineeMapper.toUpdatedTraineeResponse(trainee);

        assertEquals("Bob.Johnson", response.getUsername());
        assertEquals("Bob", response.getFirstName());
        assertEquals("Johnson", response.getLastName());
        assertFalse(response.isActive());
        assertEquals(dateOfBirth, response.getDateOfBirth());
        assertEquals("789 Oak St", response.getAddress());
        assertEquals(2, response.getTrainersList().size());
    }

}