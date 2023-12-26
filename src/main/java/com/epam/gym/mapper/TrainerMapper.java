package com.epam.gym.mapper;

import com.epam.gym.dto.trainer.TrainerDTO;
import com.epam.gym.dto.trainer.TrainerProfileResponse;
import com.epam.gym.dto.trainer.TrainerRegisterRequest;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.dto.trainer.TrainerUpdateResponse;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.util.PasswordMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainerMapper {

    private final TrainingTypeService trainingTypeService;
    private final TraineeMapper traineeMapper;

    public TrainerMapper(TrainingTypeService trainingTypeService, @Lazy TraineeMapper traineeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.traineeMapper = traineeMapper;
    }

    public Trainer toModel(TrainerRegisterRequest trainerRegister) {
        Trainer trainer = new Trainer();
        User user = new User();
        TrainingType specialization = trainingTypeService.findById(trainerRegister.getSpecializationId());
        user.setFirstName(trainerRegister.getFirstName());
        user.setLastName(trainerRegister.getLastName());
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        return trainer;
    }

    public TrainerDTO toDTO(Trainer trainer) {
        TrainerDTO trainerDTO = new TrainerDTO();
        User user = trainer.getUser();
        trainerDTO.setFirstName(user.getFirstName());
        trainerDTO.setLastName(user.getLastName());
        trainerDTO.setUsername(user.getUserName());
        trainerDTO.setSpecialization(trainer.getSpecialization());
        return trainerDTO;
    }

    public TrainerProfileResponse toProfileResponse(Trainer trainer) {
        TrainerProfileResponse response = new TrainerProfileResponse();
        User user = trainer.getUser();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setActive(user.isActive());
        response.setSpecialization(trainer.getSpecialization());
        response.setTraineeList(trainer.getTrainees()
                .stream()
                .map(traineeMapper::toDTO)
                .collect(Collectors.toList()));
        return response;
    }

    public Trainer toUpdatedTrainer(Trainer trainerToUpdate, TrainerUpdateRequest updateRequest){
        User user = trainerToUpdate.getUser();
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setUserName(updateRequest.getUsername());
        user.setPassword(PasswordMapper.toCharArray(updateRequest.getPassword()));
        user.setActive(updateRequest.isActive());
        return trainerToUpdate;
    }

    public TrainerUpdateResponse toUpdatedTrainerResponse(Trainer updated) {
        TrainerUpdateResponse response = new TrainerUpdateResponse();
        User user = updated.getUser();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setActive(user.isActive());
        response.setUsername(user.getUserName());
        response.setSpecialization(updated.getSpecialization());
        response.setTrainees(updated.getTrainees()
                .stream()
                .map(traineeMapper::toDTO)
                .collect(Collectors.toList()));
        return response;
    }

    public List<TrainerDTO> toDtoList(Collection<Trainer> trainers){
       return trainers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
