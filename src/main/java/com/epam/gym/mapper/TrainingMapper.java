package com.epam.gym.mapper;

import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.dto.training.TrainingCreateDTO;
import com.epam.gym.dto.training.TrainingDTO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingMapper {

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public TrainingMapper(TrainerService trainerService,
                          @Lazy TraineeService traineeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    public TrainingDTO toDto(Training training){
      TrainingDTO trainingDTO = new TrainingDTO();
      trainingDTO.setTrainingName(training.getTrainingName());
      trainingDTO.setDuration(training.getTrainingDuration());
      trainingDTO.setTrainingDate(training.getTrainingTime());
      trainingDTO.setTrainerName(training.getTrainer().getUser().getUserName());
      trainingDTO.setTraineeName(training.getTrainee().getUser().getUserName());
      trainingDTO.setType(training.getTrainingType());
      return trainingDTO;
   }

   public Training toModel(TrainingCreateDTO trainingCreateDTO){
      Training training = new Training();
      Trainer trainer = trainerService.getByUserName(trainingCreateDTO.getTrainerUsername());
      Trainee trainee = traineeService.getByUserName(trainingCreateDTO.getTraineeUsername());
      TrainingType trainingType = trainer.getSpecialization();
      training.setTrainingDuration(trainingCreateDTO.getDuration());
      training.setTrainingName(trainingCreateDTO.getTrainingName());
      training.setTrainingTime(trainingCreateDTO.getTrainingDate());
      training.setTrainee(trainee);
      training.setTrainer(trainer);
      training.setTrainingType(trainingType);
      return training;
   }

   public TrainingReportRequest toRequestDtoWithoutActionType(Training training){
        TrainingReportRequest clientRequest = new TrainingReportRequest();
        User user  = training.getTrainer().getUser();
        clientRequest.setUsername(user.getUserName());
        clientRequest.setFirstName(user.getFirstName());
        clientRequest.setLastName(user.getLastName());
        clientRequest.setActive(user.isActive());
        clientRequest.setDate(convertToLocalDateTime(training.getTrainingTime()));
        clientRequest.setDuration(Duration.ofMinutes(training.getTrainingDuration()));
        return clientRequest;
   }

   public List<TrainingDTO> toDtoList(Collection<Training> trainingList){
       return trainingList.stream()
               .map(this::toDto)
               .collect(Collectors.toList());
   }

   private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
