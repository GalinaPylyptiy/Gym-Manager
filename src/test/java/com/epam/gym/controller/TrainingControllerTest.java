package com.epam.gym.controller;

import com.epam.gym.dto.training.TrainingCreateDTO;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingMapper trainingMapper;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setup() {
       mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    public void testAddTraining() throws Exception {
        TrainingCreateDTO trainingCreateDTO = new TrainingCreateDTO();
        trainingCreateDTO.setTrainingName("Training name");
        trainingCreateDTO.setTraineeUsername("trainee");
        trainingCreateDTO.setTrainerUsername("trainer");
        trainingCreateDTO.setDuration(60);
        trainingCreateDTO.setTrainingDate(new Date(2023, Calendar.DECEMBER, 23));

        Training training = createTraining();

        when(trainingMapper.toModel(any())).thenReturn(training);
        doNothing().when(trainingService).save(training);

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainingCreateDTO)))
                .andExpect(status().isOk());

        Mockito.verify(trainingService, times(1)).save(training);
    }

    @Test
    void testDeleteTraining() throws Exception {

        Long trainingId = 1L;
        Training training = new Training();
        when(trainingService.getById(trainingId)).thenReturn(training);
        mockMvc.perform(delete("/trainings/{trainingId}", trainingId))
                .andExpect(status().isOk());
    }

    private Training createTraining(){
        Training training = new Training();
        training.setTrainingType(new TrainingType(1L, "Type"));
        training.setTrainee(new Trainee());
        training.setTrainer(new Trainer());
        training.setTrainingDuration(60);
        training.setTrainingTime(new Date());
        training.setTrainingName("group training");
        return training;
    }


}