package com.epam.gym.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class TrainerRegisterRequest {

    @NotEmpty(message = "Please, enter the first name...First name should not be empty")
    @JsonProperty(value = "firstName")
    private String firstName;

    @NotEmpty(message = "Please, enter the last name...Last name should not be empty")
    @JsonProperty(value = "lastName")
    private String lastName;

    @Min(value = 1,message = "The specialization id should not be less than 1 and greater than 5")
    @Max(value = 5,message = "The specialization id should not be less than 1 and greater than 5" )
    @JsonProperty(value = "specializationId")
    private Long specializationId;

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

    public Long getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Long specializationId) {
        this.specializationId = specializationId;
    }
}
