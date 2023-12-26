package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationResponse {

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "password")
    private String password;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
