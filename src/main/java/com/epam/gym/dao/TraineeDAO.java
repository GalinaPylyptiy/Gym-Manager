package com.epam.gym.dao;


import com.epam.gym.model.Trainee;

import java.util.Collection;

public interface TraineeDAO {

    Trainee findByUserName(String userName);
    Trainee findByUsernameAndPassword(String username, String password);
    Collection<Trainee> getAll();
    void save(Trainee trainee);
    void deleteByUserName(String userName);
    void update(Trainee newTraineeInfo);
}
