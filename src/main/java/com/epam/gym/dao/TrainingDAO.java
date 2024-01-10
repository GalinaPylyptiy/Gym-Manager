package com.epam.gym.dao;


import com.epam.gym.model.Training;

import java.util.Collection;


public interface TrainingDAO {

    Training getById(Long id);
    Collection<Training> getAll();
    void save(Training training);
    void update(Training newTrainingInfo);
    void delete(Long trainingId);
    Collection <Training> getTraineeTrainings(String traineeUserName);
    Collection <Training> getTrainerTrainings(String trainerUserName);
}
