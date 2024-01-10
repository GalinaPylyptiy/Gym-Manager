package com.epam.gym.mapper;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeMapperTest {

    @InjectMocks
    private TrainingTypeMapper trainingTypeMapper;


    @Test
    public void testToDTO() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Cardio");

        TrainingTypeDTO trainingTypeDTO = trainingTypeMapper.toDTO(trainingType);

        assertEquals(1L, trainingTypeDTO.getId());
        assertEquals("Cardio", trainingTypeDTO.getTypeName());
    }

    @Test
    public void testToDtoList() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1L);
        trainingType1.setTrainingTypeName("Cardio");

        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2L);
        trainingType2.setTrainingTypeName("Strength");

        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(trainingType1);
        trainingTypes.add(trainingType2);

        List<TrainingTypeDTO> trainingTypeDTOList = trainingTypeMapper.toDtoList(trainingTypes);

        assertEquals(2, trainingTypeDTOList.size());
        assertEquals(1L, trainingTypeDTOList.get(0).getId());
        assertEquals("Cardio", trainingTypeDTOList.get(0).getTypeName());
        assertEquals(2L, trainingTypeDTOList.get(1).getId());
        assertEquals("Strength", trainingTypeDTOList.get(1).getTypeName());
    }
}
