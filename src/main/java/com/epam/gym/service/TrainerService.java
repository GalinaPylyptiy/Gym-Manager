package com.epam.gym.service;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;

import java.util.Collection;

public interface TrainerService {

    RegistrationResponse save(Trainer trainer);
    Collection<Trainer> getAllActiveTrainers();
    Trainer getByUserName(String userName);
    Collection<Training> getAllTrainings(String userName);
    void update(Trainer newTrainerInfo);
    Trainer getByUsernameAndPassword(String username, String password);
}
