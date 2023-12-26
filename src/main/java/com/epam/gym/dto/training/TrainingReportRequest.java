package com.epam.gym.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class TrainingReportRequest implements Serializable{

    @JsonProperty(value = "trainerUsername")
    private String username;

    @JsonProperty(value = "trainerFirstName")
    private String firstName;

    @JsonProperty(value = "trainerLastName")
    private String lastName;

    @JsonProperty(value = "isActive")
    private Boolean isActive;

    @JsonProperty(value = "trainingDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @JsonProperty(value = "trainingDuration")
    private Duration duration;

    @JsonProperty(value = "actionType")
    private ActionType actionType;

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
