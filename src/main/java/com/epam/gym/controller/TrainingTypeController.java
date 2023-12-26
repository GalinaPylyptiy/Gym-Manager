package com.epam.gym.controller;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.mapper.TrainingTypeMapper;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/trainingTypes")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeController(TrainingTypeService trainingTypeService, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @GetMapping
    @Operation(summary = "Get all training types")
    public List<TrainingTypeDTO> getAll(){
        Collection<TrainingType> trainingTypes = trainingTypeService.getAll();
        return trainingTypeMapper.toDtoList(trainingTypes);
    }
}
