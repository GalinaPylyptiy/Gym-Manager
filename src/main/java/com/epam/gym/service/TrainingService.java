package com.epam.gym.service;
import com.epam.gym.model.Training;

import java.util.Collection;

public interface TrainingService {

    Collection<Training> getAll();
    void save(Training training);
    void delete(Training training);
    Training getById(Long id);

}
