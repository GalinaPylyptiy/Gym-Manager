package com.epam.gym.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dao.TraineeDAO;
import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dao.UserDAO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.mapper.UserMapper;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TrainingDAO trainingDAO;



    private Trainee createTrainee() {
        Trainee trainee = new Trainee();
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        trainee.setDateOfBirth(dateOfBirth);
        User user = new User();
        user.setId(1L);
        user.setFirstName("Bob");
        user.setLastName("Smith");
        user.setUserName("Bob.Smith");
        user.setPassword("password".toCharArray());
        trainee.setUser(user);
        return trainee;
    }

    @Test
    public void testGetByUserName() {

        String userName = "Bob.Smith";
        Trainee trainee = createTrainee();
        when(traineeDAO.findByUserName(userName)).thenReturn(trainee);

        Trainee result = traineeService.getByUserName(userName);

        assertNotNull(result);
        assertEquals(trainee.getDateOfBirth(), result.getDateOfBirth());
    }

    @Test
    public void testGetByUserNameAndPassword() {

        String userName = "Bob.Smith";
        String password = "password";
        Trainee trainee = createTrainee();

        when(traineeDAO.findByUserName(userName)).thenReturn(trainee);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);

        Trainee result = traineeService.getByUsernameAndPassword(userName, password);

        assertNotNull(result);
        assertEquals(trainee.getUser().getPassword(), result.getUser().getPassword());
        assertEquals(trainee.getUser().getUserName(), result.getUser().getUserName());
        assertEquals(trainee.getDateOfBirth(), result.getDateOfBirth());
    }

    @Test
    public void testAssignTrainers() {
        Trainee trainee = createTrainee();
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());

        traineeService.assignTrainers(trainee, trainers);

        assertEquals(trainers.size(), trainee.getTrainers().size());
    }

    @Test
    public void testGetAll() {
        List<Trainee> mockTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeDAO.getAll()).thenReturn(mockTrainees);

        Collection<Trainee> result = traineeService.getAll();

        assertNotNull(result);
        assertEquals(mockTrainees.size(), result.size());
    }

    @Test
    public void testAdd() {
        Trainee trainee = createTrainee();
        RegistrationResponse response = new RegistrationResponse();

        when(userMapper.toRegisterResponse(trainee.getUser())).thenReturn(response);

        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        traineeService.save(trainee);

        assertNotNull(trainee.getUser().getPassword());
        assertTrue(trainee.getUser().isActive());
    }

    @Test
    public void testDeleteByUserName() {
        Trainee trainee = createTrainee();

        traineeService.delete(trainee);

        verify(traineeDAO, times(1)).deleteByUserName(trainee.getUser().getUserName());
    }

    @Test
    public void testUpdate() {
        Trainee newTraineeInfo = createTrainee();
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserName("Test.User");
        newTraineeInfo.setUser(user);

        doNothing().when(traineeDAO).update(newTraineeInfo);
        traineeService.update(newTraineeInfo);

        assertNotNull(newTraineeInfo.getUser().getUserName());
        assertEquals(newTraineeInfo.getUser().getUserName(), "Test.User");
    }

    @Test
    public void testGetAllTrainings() {
        String userName = "testUser";
        List<Training> mockTrainings = Arrays.asList(new Training(), new Training());
        when(trainingDAO.getTraineeTrainings(userName)).thenReturn(mockTrainings);

        Collection<Training> result = traineeService.getAllTrainings(userName);

        assertNotNull(result);
        assertEquals(mockTrainings.size(), result.size());
    }

    @Test
    public void testUpdateTrainersList() {
        Trainee trainee = createTrainee();
        List<Trainer> newTrainersList = Arrays.asList(new Trainer(), new Trainer());

        traineeService.updateTrainersList(trainee, newTrainersList);

        assertEquals(newTrainersList.size(), trainee.getTrainers().size());
        assertEquals(newTrainersList, trainee.getTrainers());
    }
}


