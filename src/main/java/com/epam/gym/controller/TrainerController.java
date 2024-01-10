package com.epam.gym.controller;

import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainer.TrainerDTO;
import com.epam.gym.dto.trainer.TrainerProfileResponse;
import com.epam.gym.dto.trainer.TrainerRegisterRequest;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.dto.trainer.TrainerUpdateResponse;
import com.epam.gym.dto.training.TrainingDTO;
import com.epam.gym.mapper.TrainerMapper;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/trainers", produces = {"application/JSON"})
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TrainerController(TrainerService trainerService, TrainerMapper trainerMapper, TrainingMapper trainingMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @PostMapping
    @Operation(summary = "Register as a trainer")
    public RegistrationResponse register(@Valid @RequestBody TrainerRegisterRequest trainerRegister){
       Trainer trainer = trainerMapper.toModel(trainerRegister);
       return trainerService.save(trainer);
    }

    @GetMapping
    @Operation(summary = "Get trainer`s profile by username and password")
    public TrainerProfileResponse getProfile(@NotNull @RequestParam String username,
                                             @NotNull @RequestParam String password){
        Trainer trainer = trainerService.getByUsernameAndPassword(username,password);
        return trainerMapper.toProfileResponse(trainer);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer`s profile info")
    public TrainerUpdateResponse update(@PathVariable("username") String username,
                                        @Valid @RequestBody TrainerUpdateRequest updateRequest){
       Trainer trainerToUpdate = trainerService.getByUserName(username);
       Trainer updated = trainerMapper.toUpdatedTrainer(trainerToUpdate, updateRequest);
       trainerService.update(updated);
       return trainerMapper.toUpdatedTrainerResponse(updated);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all not assigned on a specific trainee active trainers ")
    public List<TrainerDTO> getFreeAndActive(@NotNull @RequestParam String username,
                                             @NotNull @RequestParam String password){
        Collection<Trainer> trainers =  trainerService.getAllActiveTrainers();
        return trainerMapper.toDtoList(trainers);
    }

    @GetMapping("/trainings")
    @Operation(summary = "Get all trainer`s trainings" )
    public List<TrainingDTO> getTrainings(@NotNull @RequestParam String username,
                                          @NotNull @RequestParam String password) {
        Trainer trainer = trainerService.getByUsernameAndPassword(username, password);
        Collection<Training> trainings = trainerService.getAllTrainings(trainer.getUser().getUserName());
        return trainingMapper.toDtoList(trainings);
    }

}
