package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public class ChangePasswordRequest {

    @NotEmpty(message = "Please, enter the user name... User name should not be empty")
    @JsonProperty(value = "username")
    private String username;

    @NotEmpty(message = "Please, enter the old password...Password should not be empty")
    @JsonProperty(value = "oldPassword")
    private String oldPassword;

    @NotEmpty(message = "Please, enter the new  password... Password should not be empty")
    @JsonProperty(value = "newPassword")
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
