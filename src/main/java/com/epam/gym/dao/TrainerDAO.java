package com.epam.gym.dao;
import com.epam.gym.model.Trainer;

import java.util.Collection;

public interface TrainerDAO {

   Trainer findByUserName(String userName);
   Trainer findByUsernameAndPassword(String username, String password);
   Collection<Trainer> getFreeActiveTrainers();
   Collection<Trainer> getAll();
   void save(Trainer trainer);
   void update(Trainer newTrainerInfo);

}
