package com.epam.gym.dao;


import com.epam.gym.model.TrainingType;

import java.util.Collection;
import java.util.Optional;

public interface TrainingTypeDAO {

    Optional<TrainingType> findById(Long id);
    Collection<TrainingType> getAll();
}
