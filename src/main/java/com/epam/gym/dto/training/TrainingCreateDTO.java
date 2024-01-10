package com.epam.gym.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

public class TrainingCreateDTO {

    @NotEmpty(message = "Please, enter the training name...Training name should not be empty")
    @JsonProperty(value = "trainingName")
    private String trainingName;

    @NotEmpty(message = "Please, enter the trainee user name..Trainee user name should not be empty")
    @JsonProperty(value = "traineeUsername")
    private String traineeUsername;

    @NotEmpty(message = "Please, enter the trainer user name.. .Trainer user name should not be empty")
    @JsonProperty(value = "trainerUsername")
    private String trainerUsername;

    @NotNull(message = "Please, enter the training date..Training date name should not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Future(message = "Training date should be in the future")
    @JsonProperty(value = "trainingDate")
    private Date trainingDate;

    @Positive(message = "Training duration should be positive")
    @Max(value=120, message = "Training duration can not be more than 120 minutes")
    @JsonProperty(value = "duration")
    private int duration;

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
