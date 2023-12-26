package com.epam.gym.mapper;

import com.epam.gym.dto.trainee.TraineeDTO;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.dto.trainee.TraineeRegisterRequest;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.User;
import com.epam.gym.util.PasswordMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TraineeMapper {

    private final TrainerMapper trainerMapper;

    public TraineeMapper(@Lazy TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    public Trainee toModel(TraineeRegisterRequest traineeRegister) {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName(traineeRegister.getFirstName());
        user.setLastName(traineeRegister.getLastName());
        trainee.setUser(user);
        Date dateOfBirth = traineeRegister.getDateOfBirth();
        if (dateOfBirth != null) {
            trainee.setDateOfBirth(dateOfBirth);
        }
        trainee.setAddress(traineeRegister.getAddress());
        return trainee;
    }

    public Trainee toUpdatedTrainee(Trainee traineeToUpdate, TraineeUpdateRequest traineeUpdate){
        User user = traineeToUpdate.getUser();
        user.setFirstName(traineeUpdate.getFirstName());
        user.setLastName(traineeUpdate.getLastName());
        user.setUserName(traineeUpdate.getUsername());
        user.setPassword(PasswordMapper.toCharArray(traineeUpdate.getPassword()));
        user.setActive(traineeUpdate.isActive());
        traineeToUpdate.setAddress(traineeUpdate.getAddress());
        Date dateOfBirth = traineeUpdate.getDateOfBirth();
        if (dateOfBirth != null) {
            traineeToUpdate.setDateOfBirth(dateOfBirth);
        }
        return traineeToUpdate;
    }

    public TraineeDTO toDTO(Trainee trainee){
        TraineeDTO traineeDTO = new TraineeDTO();
        User user = trainee.getUser();
        traineeDTO.setFirstName(user.getFirstName());
        traineeDTO.setLastName(user.getLastName());
        traineeDTO.setUsername(user.getUserName());
        return traineeDTO;
    }

    public TraineeProfileResponse toProfileResponse(Trainee trainee) {
        TraineeProfileResponse response = new TraineeProfileResponse();
        User user = trainee.getUser();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setActive(user.isActive());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setTrainersList(trainee.getTrainers()
                .stream()
                .map(trainerMapper::toDTO)
                .collect(Collectors.toList()));
        return response;
    }

    public TraineeUpdateResponse toUpdatedTraineeResponse(Trainee trainee){
        TraineeUpdateResponse response = new TraineeUpdateResponse();
        User user = trainee.getUser();
        response.setUsername(user.getUserName());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setActive(user.isActive());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setTrainersList(trainee.getTrainers()
                .stream()
                .map(trainerMapper::toDTO)
                .collect(Collectors.toList()));
        return response;
    }

        }
