package com.epam.gym.controller;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.mapper.TrainingTypeMapper;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController).build();
    }

    @Test
    public void testGetAllTrainingTypes() throws Exception {
        List<TrainingType> mockTrainingTypes = Collections.singletonList(new TrainingType());
        List<TrainingTypeDTO> mockTrainingTypeDTOs = Collections.singletonList(new TrainingTypeDTO());
        when(trainingTypeService.getAll()).thenReturn(mockTrainingTypes);
        when(trainingTypeMapper.toDtoList(mockTrainingTypes)).thenReturn(mockTrainingTypeDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingTypes"))
                .andExpect(status().isOk());

    }

}