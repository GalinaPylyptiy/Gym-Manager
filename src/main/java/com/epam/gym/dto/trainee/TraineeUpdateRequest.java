package com.epam.gym.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public class TraineeUpdateRequest {

    @NotEmpty(message = "Please, enter the username... Username should not be empty!")
    @JsonProperty(value = "username")
    private String username;

    @NotEmpty(message = "Please, enter the first name... First name should not be empty")
    @JsonProperty(value = "firstName")
    private String firstName;

    @NotEmpty(message = "Please, enter the last name... Last name should not be empty")
    @JsonProperty(value = "lastName")
    private String lastName;

    @NotEmpty(message = "Please, enter new password...Password should not be empty")
    @JsonProperty(value = "password")
    private String password;

    @NotNull(message = "Field should not be empty.Enter 'true' or 'false .... ")
    @JsonProperty(value = "isActive")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth should be past date")
    @JsonProperty(value = "dateOfBirth")
    private Date dateOfBirth;

    @JsonProperty(value = "address")
    private String address;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
