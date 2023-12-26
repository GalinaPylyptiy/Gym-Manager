package com.epam.gym.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;

import java.util.Date;

public class TraineeRegisterRequest {

    @NotEmpty(message = "Please, enter the first name.. .First name should not be empty")
    @JsonProperty(value = "firstName")
    private String firstName;

    @NotEmpty(message = "Please, enter the last name... Last name should not be empty")
    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth should be in the past...")
    @JsonProperty(value = "dateOfBirth")
    private Date dateOfBirth;

    @JsonProperty(value = "address")
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
