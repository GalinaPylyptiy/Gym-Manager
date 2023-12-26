package com.epam.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ChangeActivityRequest {

    @NotEmpty(message = "Please, enter the user name.. User name should not be empty")
    private String username;

    @NotEmpty(message = "Please, enter the password... Password should not be empty")
    private String password;

    @NotNull(message = "Field should not be empty. Enter 'true' or 'false .... ")
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
