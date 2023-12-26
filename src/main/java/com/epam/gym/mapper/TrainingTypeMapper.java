package com.epam.gym.mapper;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.model.TrainingType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingTypeMapper {

    public TrainingTypeDTO toDTO(TrainingType trainingType){
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
        trainingTypeDTO.setId(trainingType.getId());
        trainingTypeDTO.setTypeName(trainingType.getTrainingTypeName());
        return trainingTypeDTO;
    }

    public List<TrainingTypeDTO> toDtoList(Collection<TrainingType> trainingTypes){
        return trainingTypes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
