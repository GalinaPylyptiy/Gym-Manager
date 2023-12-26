package com.epam.gym.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dao.TrainerDAO;
import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dao.UserDAO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.mapper.UserMapper;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.UserService;
import com.epam.gym.util.PasswordMapper;
import com.epam.gym.util.UserNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerDAO trainerDAO;

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

    private Trainer createTrainer() {
        Trainer trainer = new Trainer();
        TrainingType specialization = new TrainingType();
        specialization.setId(1L);
        specialization.setTrainingTypeName("yoga");
        User user = new User();
        user.setFirstName("Helen");
        user.setLastName("Black");
        user.setUserName(UserNameGenerator.generateUserName("Helen","Black"));
        user.setPassword("password".toCharArray());
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        return trainer;
    }

    @Test
    public void testAddTrainer() {
        Trainer trainer = createTrainer();
        RegistrationResponse response = new RegistrationResponse();

        when(userMapper.toRegisterResponse(trainer.getUser())).thenReturn(response);

        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        trainerService.save(trainer);

        assertNotNull(trainer.getUser().getPassword());
        assertTrue(trainer.getUser().isActive());

    }

    @Test
    public void testUpdateTrainer() {
        Trainer newTrainerInfo = createTrainer();
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserName("Test.User");
        newTrainerInfo.setUser(user);

        doNothing().when(trainerDAO).update(newTrainerInfo);
        trainerService.update(newTrainerInfo);

        verify(trainerDAO, times(1)).update(newTrainerInfo);
        assertNotNull(newTrainerInfo.getUser().getUserName());
        assertEquals(newTrainerInfo.getUser().getUserName(), "Test.User");
    }


    @Test
    public void testGetAllActiveTrainers() {
        List<Trainer> mockTrainers = List.of(new Trainer(), new Trainer());

        when(trainerDAO.getFreeActiveTrainers()).thenReturn(mockTrainers);

        Collection<Trainer> result = trainerService.getAllActiveTrainers();

        assertNotNull(result);
        assertEquals(mockTrainers.size(), result.size());
    }

    @Test
    public void testGetByUserName() {

        String userName = "TestUser";
        Trainer mockTrainer = createTrainer();

        when(trainerDAO.findByUserName(userName)).thenReturn(mockTrainer);

        Trainer result = trainerService.getByUserName(userName);

        assertNotNull(result);
        assertEquals(mockTrainer, result);
    }

    @Test
    public void testGetByUserNameAndPassword() {

        String userName = "Helen.Black";
        String password = "password";
        Trainer trainer = createTrainer();
        when(trainerDAO.findByUserName(userName)).thenReturn(trainer);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);

        Trainer result = trainerService.getByUsernameAndPassword(userName, password);

        assertNotNull(result);
        assertEquals(userName, result.getUser().getUserName());
        assertEquals(trainer.getUser().getPassword(), result.getUser().getPassword());
        assertEquals(trainer.getUser().getUserName(), result.getUser().getUserName());
        assertEquals(trainer.getSpecialization(), result.getSpecialization());
    }

    @Test
    public void testGetAllTrainings() {
        String userName = "testUser";
        List<Training> mockTrainings = new ArrayList<>();
        mockTrainings.add(new Training());
        mockTrainings.add(new Training());

        when(trainingDAO.getTrainerTrainings(userName)).thenReturn(mockTrainings);

        Collection<Training> result = trainerService.getAllTrainings(userName);

        assertNotNull(result);
        assertEquals(mockTrainings.size(), result.size());
    }

}
