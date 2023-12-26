package com.epam.gym.dto.training;

import com.epam.gym.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TrainingDTO {

    @JsonProperty(value = "trainingName")
    private String trainingName;

    @JsonProperty(value = "trainingDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date trainingDate;

    @JsonProperty(value = "type")
    private TrainingType type;

    @JsonProperty(value = "duration")
    private int duration;

    @JsonProperty(value = "trainerName")
    private String trainerName;

    @JsonProperty(value = "traineeName")
    private String traineeName;

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
}
