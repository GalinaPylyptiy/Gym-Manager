package com.epam.gym.dto.trainer;

import com.epam.gym.dto.trainee.TraineeDTO;
import com.epam.gym.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TrainerUpdateResponse {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "specialization")
    private TrainingType specialization;

    @JsonProperty(value = "isActive")
    private boolean isActive;

    @JsonProperty(value = "trainees")
    private List<TraineeDTO> trainees;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<TraineeDTO> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeDTO> trainees) {
        this.trainees = trainees;
    }
}
