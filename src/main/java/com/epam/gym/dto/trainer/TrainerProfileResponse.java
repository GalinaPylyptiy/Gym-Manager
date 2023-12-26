package com.epam.gym.dto.trainer;

import com.epam.gym.dto.trainee.TraineeDTO;
import com.epam.gym.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TrainerProfileResponse {

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "specialization")
    private TrainingType specialization;

    @JsonProperty(value = "isActive")
    private Boolean isActive;

    @JsonProperty(value = "traineeList")
    private List<TraineeDTO> traineeList;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<TraineeDTO> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<TraineeDTO> traineeList) {
        this.traineeList = traineeList;
    }
}
