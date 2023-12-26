package com.epam.gym.service;


import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;

import java.util.Collection;
import java.util.List;

public interface TraineeService {

    Trainee getByUserName(String userName);
    Trainee getByUsernameAndPassword(String username, String password);
    void assignTrainers(Trainee trainee, List<Trainer> trainers);
    Collection<Trainee> getAll();
    RegistrationResponse save(Trainee trainee);
    void delete(Trainee trainee);
    void update(Trainee newTraineeInfo);
    Collection<Training> getAllTrainings(String userName);
//    @Deprecated(forRemoval = true)
    void updateTrainersList(Trainee trainee, List<Trainer> newTrainersList);


}
