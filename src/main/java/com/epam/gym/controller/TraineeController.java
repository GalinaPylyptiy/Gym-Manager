package com.epam.gym.controller;

import com.epam.gym.client.TrainingReportClient;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.dto.trainee.TraineeRegisterRequest;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
import com.epam.gym.dto.trainee.TraineeUpdateTrainersListRequest;
import com.epam.gym.dto.trainer.TrainerDTO;
import com.epam.gym.dto.training.TrainingDTO;
import com.epam.gym.mapper.TraineeMapper;
import com.epam.gym.mapper.TrainerMapper;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/trainees", produces = {"application/JSON"})
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;


    public TraineeController(TraineeService traineeService,
                             TrainerService trainerService,
                             TraineeMapper traineeMapper,
                             TrainerMapper trainerMapper,
                             TrainingMapper trainingMapper) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @PostMapping
    @Operation(summary = "Register as a trainee")
    public RegistrationResponse register(@Valid @RequestBody TraineeRegisterRequest traineeRegister) {
        Trainee trainee = traineeMapper.toModel(traineeRegister);
        return traineeService.save(trainee);
    }

    @GetMapping
    @Operation(summary = "Get trainee profile by username and password")
    public TraineeProfileResponse getProfile(@NotNull @RequestParam String username,
                                             @NotNull @RequestParam String password) {
        Trainee trainee = traineeService.getByUsernameAndPassword(username, password);
        return traineeMapper.toProfileResponse(trainee);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee info")
    public TraineeUpdateResponse update(@PathVariable("username") String username,
                                        @Valid @RequestBody TraineeUpdateRequest traineeUpdateRequest) {
        Trainee traineeToUpdate = traineeService.getByUserName(username);
        Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(traineeToUpdate, traineeUpdateRequest);
        traineeService.update(updatedTrainee);
        return traineeMapper.toUpdatedTraineeResponse(traineeToUpdate);
    }

    @DeleteMapping
    @Operation(summary = "Delete trainee by username and password")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@NotNull @RequestParam String username,
                       @NotNull @RequestParam String password) {
        Trainee trainee = traineeService.getByUsernameAndPassword(username, password);
        traineeService.delete(trainee);
    }

    @PutMapping("/updateTrainers")
    @Operation(summary = "Update trainee`s trainers list")
    public List<TrainerDTO> updateTrainersList(@Valid @RequestBody TraineeUpdateTrainersListRequest request) {
        Trainee trainee = traineeService.getByUserName(request.getTraineeUsername());
        List<Trainer> trainersList = request.getTrainersUsernameList()
                .stream()
                .map(trainerService::getByUserName)
                .collect(Collectors.toList());
        traineeService.updateTrainersList(trainee, trainersList);
        return trainerMapper.toDtoList(trainersList);
    }

    @GetMapping("/trainings")
    @Operation(summary = "Get all trainee`s trainings")
    public List<TrainingDTO> getTrainings(@NotNull @RequestParam String username,
                                          @NotNull @RequestParam String password) {
        Trainee trainee = traineeService.getByUsernameAndPassword(username, password);
        Collection<Training> trainings = traineeService.getAllTrainings(trainee.getUser().getUserName());
        return trainingMapper.toDtoList(trainings);
    }

}
