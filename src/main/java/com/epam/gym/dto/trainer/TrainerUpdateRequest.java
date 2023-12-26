package com.epam.gym.dto.trainer;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TrainerUpdateRequest {

    @NotEmpty(message = "Please, enter the user name.. User name should not be empty")
    @JsonProperty(value = "username")
    private String username;

    @NotEmpty(message = "Please, enter the password... Password should not be empty")
    @JsonProperty(value = "password")
    private String password;

    @NotEmpty(message = "Please, enter the first name... First name should not be empty")
    @JsonProperty(value = "firstName")
    private String firstName;

    @NotEmpty(message = "Please, enter the first name... Last name should not be empty")
    @JsonProperty(value = "lastName")
    private String lastName;

    @NotNull(message = "Field should not be empty.Enter 'true' or 'false .... ")
    @JsonProperty(value = "isActive")
    private Boolean isActive;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
