package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponseDTO {

    @JsonProperty(value = "jwt")
    private String jwt;

    public JwtResponseDTO() {
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

}
