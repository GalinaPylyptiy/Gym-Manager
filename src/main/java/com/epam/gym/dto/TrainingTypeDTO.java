package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainingTypeDTO {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "typeName")
    private String typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
