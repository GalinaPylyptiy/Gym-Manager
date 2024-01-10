package com.epam.gym.dto.trainee;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TraineeUpdateTrainersListRequest {

    @NotEmpty(message = "Username should not be empty")
    @NotNull(message = "Please, enter the username... ")
    @JsonProperty(value = "traineeUsername")
    private String traineeUsername;

    @NotEmpty(message = "List of trainers` user names should not be empty")
    @NotNull(message = "Please, enter the trainers` user names... ")
    @JsonProperty(value = "trainersUsernameList")
    private List<String> trainersUsernameList;

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<String> getTrainersUsernameList() {
        return trainersUsernameList;
    }

    public void setTrainersUsernameList(List<String> trainersUsernameList) {
        this.trainersUsernameList = trainersUsernameList;
    }
}
