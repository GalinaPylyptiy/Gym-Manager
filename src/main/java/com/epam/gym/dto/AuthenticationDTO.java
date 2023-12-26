package com.epam.gym.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public class AuthenticationDTO {

    @NotEmpty(message = "Please, enter the username... Username should not be empty")
    @JsonProperty(value = "username")
    private String username;


    @NotEmpty(message = "Please, enter new password... Password should not be empty")
    @JsonProperty(value = "password")
    private String password;

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
}
