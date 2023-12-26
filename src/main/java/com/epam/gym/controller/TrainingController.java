package com.epam.gym.controller;

import com.epam.gym.dto.training.TrainingCreateDTO;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Training;
import com.epam.gym.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    public TrainingController(TrainingService trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    @PostMapping
    @Operation(summary = "Add new training")
    public void add( @Valid @RequestBody TrainingCreateDTO trainingCreateDTO){
        Training training = trainingMapper.toModel(trainingCreateDTO);
        trainingService.save(training);
    }

    @DeleteMapping("/{trainingId}")
    public void delete (@PathVariable("trainingId") Long trainingId){
        Training training = trainingService.getById(trainingId);
        trainingService.delete(training);
    }

}
