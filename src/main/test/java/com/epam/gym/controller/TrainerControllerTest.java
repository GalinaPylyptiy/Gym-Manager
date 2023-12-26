package com.epam.gym.controller;

import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainer.TrainerProfileResponse;
import com.epam.gym.dto.trainer.TrainerRegisterRequest;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.mapper.TrainerMapper;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TrainerService;
import com.epam.gym.util.UserNameGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    public void testRegister() throws Exception {
        TrainerRegisterRequest request = new TrainerRegisterRequest();
        request.setFirstName("Bob");
        request.setLastName("Smith");
        request.setSpecializationId(2L);
        Trainer trainer = createTrainer();

        doReturn(trainer).when(trainerMapper).toModel(Mockito.any(TrainerRegisterRequest.class));

        when(trainerService.save(any())).thenReturn(new RegistrationResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(trainerService, times(1)).save(trainer);

    }

    @Test
    void testGetProfile() throws Exception {
        String username = "testUsername";
        String password = "testPassword";

        Trainer trainer = createTrainer();
        TrainerProfileResponse expectedResponse = new TrainerProfileResponse();
        expectedResponse.setFirstName("Kate");
        expectedResponse.setLastName("Smith");
        expectedResponse.setSpecialization(new TrainingType(1L, "Cardio"));

        doReturn(trainer).when(trainerService).getByUsernameAndPassword(username, password);
        doReturn(expectedResponse).when(trainerMapper).toProfileResponse(trainer);

        MvcResult result = mockMvc.perform(get("/trainers")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();

        assertThat(content).isNotNull();
        assertThat(content).contains("Kate");
    }

    @Test
    void testUpdate() throws Exception {
        String username = "testUsername";
        TrainerUpdateRequest updateRequest = new TrainerUpdateRequest();
        updateRequest.setUsername("Bob.Smith");
        updateRequest.setFirstName("Bob");
        updateRequest.setLastName("Smith");
        updateRequest.setActive(true);
        updateRequest.setPassword("password");

        Trainer trainerToUpdate = createTrainer();
        trainerToUpdate.getUser().setUserName(username);

        doReturn(trainerToUpdate).when(trainerService).getByUserName(Mockito.anyString());
        doReturn(trainerToUpdate).when(trainerMapper).toUpdatedTrainer(Mockito.any(), Mockito.any(TrainerUpdateRequest.class));
        doNothing().when(trainerService).update(trainerToUpdate);

        mockMvc.perform(put("/trainers/{username}", trainerToUpdate.getUser().getUserName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFreeAndActive() throws Exception {
        String username = "testUsername";
        String password = "testPassword";
        List<Trainer> activeTrainers = createActiveTrainers();

        doReturn(activeTrainers).when(trainerService).getAllActiveTrainers();
        doReturn(activeTrainers).when(trainerMapper).toDtoList(activeTrainers);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainers/active")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<Trainer> createActiveTrainers() {
        return List.of(createTrainer(), createTrainer());
    }

    @Test
    void testGetTrainings() throws Exception {
        String username = "testUsername";
        String password = "testPassword";

        Trainer trainer = createTrainer();
        List<Training> trainings = createTrainings();

        doReturn(trainer).when(trainerService).getByUsernameAndPassword(username, password);
        doReturn(trainings).when(trainerService).getAllTrainings(trainer.getUser().getUserName());
        doReturn(trainings).when(trainingMapper).toDtoList(trainings);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainers/trainings")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<Training> createTrainings() {
        return new ArrayList<>();
    }


    private Trainer createTrainer() {
        Trainer trainer = new Trainer();
        TrainingType specialization = new TrainingType();
        specialization.setId(1L);
        specialization.setTrainingTypeName("yoga");
        User user = new User();
        user.setFirstName("Helen");
        user.setLastName("Black");
        user.setUserName(UserNameGenerator.generateUserName("Helen", "Black"));
        user.setPassword("password".toCharArray());
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        return trainer;
    }
}