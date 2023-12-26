package com.epam.gym.controller;

import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.dto.trainee.TraineeRegisterRequest;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateTrainersListRequest;
import com.epam.gym.mapper.TraineeMapper;
import com.epam.gym.mapper.TrainerMapper;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

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
    public void testRegisterTrainee() throws Exception {
        TraineeRegisterRequest request = new TraineeRegisterRequest();
        request.setFirstName("Bob");
        request.setLastName("Smith");
        request.setAddress("Address");
        request.setDateOfBirth(new Date(2000-12-17));

        Trainee trainee = new Trainee();

        when(traineeMapper.toModel(any())).thenReturn(trainee);

        when(traineeService.save(any())).thenReturn(new RegistrationResponse());

        mockMvc.perform(post("/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(traineeService, times(1)).save(trainee);
    }

    @Test
    void testGetProfile() throws Exception {

        TraineeProfileResponse expectedResponse = new TraineeProfileResponse();
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");

        when(traineeService.getByUsernameAndPassword(anyString(), anyString())).thenReturn(new Trainee());
        when(traineeMapper.toProfileResponse(any())).thenReturn(expectedResponse);

        String username = "john_doe";
        String password = "password";

        MvcResult result = mockMvc.perform(get("/trainees")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();

        assertThat(content).isNotNull();
        assertThat(content).contains("John");
    }

    @Test
    void testUpdate() throws Exception {
        String username = "testUsername";
        TraineeUpdateRequest updateRequest = new TraineeUpdateRequest();
        Date dateOfBirth = new Date(85, Calendar.JUNE, 3);
        updateRequest.setDateOfBirth(dateOfBirth);
        updateRequest.setUsername("Bob.Smith");
        updateRequest.setFirstName("Bob");
        updateRequest.setLastName("Smith");
        updateRequest.setActive(true);
        updateRequest.setPassword("password");
        updateRequest.setAddress("new address");

        Trainee traineeToUpdate = createTrainee();
        traineeToUpdate.getUser().setUserName(username);

        doReturn(traineeToUpdate).when(traineeService).getByUserName(anyString());
        doReturn(traineeToUpdate).when(traineeMapper).toUpdatedTrainee(any(), any(TraineeUpdateRequest.class));
        doNothing().when(traineeService).update(traineeToUpdate);

        mockMvc.perform(put("/trainees/{username}", traineeToUpdate.getUser().getUserName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        String username = "testUsername";
        String password = "testPassword";

        Trainee trainee = createTrainee();

        doReturn(trainee).when(traineeService).getByUsernameAndPassword(username, password);
        doNothing().when(traineeService).delete(trainee);

        mockMvc.perform(delete("/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(traineeService, times(1)).delete(trainee);
    }

    @Test
    void testUpdateTrainersList() throws Exception {
        TraineeUpdateTrainersListRequest updateRequest = new TraineeUpdateTrainersListRequest();
        updateRequest.setTraineeUsername("testTraineeUsername");
        updateRequest.setTrainersUsernameList(Arrays.asList("trainer1", "trainer2"));

        Trainee trainee = createTrainee();
        doReturn(trainee).when(traineeService).getByUserName("testTraineeUsername");

        Trainer trainer1 = createTrainer("trainer1");
        Trainer trainer2 = createTrainer("trainer2");
        doReturn(trainer1).when(trainerService).getByUserName("trainer1");
        doReturn(trainer2).when(trainerService).getByUserName("trainer2");

        doNothing().when(traineeService).updateTrainersList(trainee, Arrays.asList(trainer1, trainer2));

        mockMvc.perform(put("/trainees/updateTrainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainings() throws Exception {
        String username = "testUsername";
        String password = "testPassword";

        Trainee trainee = createTrainee();
        List<Training> trainings = createTrainings();

        doReturn(trainee).when(traineeService).getByUsernameAndPassword(username, password);
        doReturn(trainings).when(traineeService).getAllTrainings(trainee.getUser().getUserName());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trainees/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();

        assertThat(content).isNotNull();

    }

    private List<Training> createTrainings() {
        return List.of(new Training(), new Training());
    }

    private Trainer createTrainer(String username) {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUserName(username);
        return trainer;
    }

}
