package com.epam.gym.dto.trainee;

import com.epam.gym.dto.trainer.TrainerDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class TraineeProfileResponse {

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "dateOfBirth")
    private Date dateOfBirth;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "trainersList")
    private List<TrainerDTO> trainersList;

    @JsonProperty(value = "isActive")
    private Boolean isActive;

    public TraineeProfileResponse() {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<TrainerDTO> getTrainersList() {
        return trainersList;
    }

    public void setTrainersList(List<TrainerDTO> trainersList) {
        this.trainersList = trainersList;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
