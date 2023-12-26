package com.epam.gym.service;

import com.epam.gym.model.TrainingType;

import java.util.Collection;

public interface TrainingTypeService {

    TrainingType findById(Long id);
    Collection<TrainingType> getAll();
}
