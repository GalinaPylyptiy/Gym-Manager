package com.epam.gym.service.impl;

import com.epam.gym.dao.TrainerDAO;
import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.exception.PasswordMatchingException;
import com.epam.gym.mapper.UserMapper;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.PasswordMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;

@Service(value = "trainerService")
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final static Log LOG = LogFactory.getLog(TraineeServiceImpl.class);


    public TrainerServiceImpl(TrainerDAO trainerDAO,
                              TrainingDAO trainingDAO,
                              UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.trainerDAO = trainerDAO;
        this.trainingDAO = trainingDAO;
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Collection<Trainer> getAllActiveTrainers() {
            LOG.info("Getting all not assigned on specific trainees active trainers...");
            return trainerDAO.getFreeActiveTrainers();
    }

    @Override
    public Trainer getByUserName(String userName) {
            LOG.info("Getting trainer by username: " + userName);
            return trainerDAO.findByUserName(userName);
    }

    @Override
    public Collection<Training> getAllTrainings(String userName) {
            LOG.info("Getting all the trainer`s trainings by username: "+ userName);
            return trainingDAO.getTrainerTrainings(userName);
    }

    @Override
    public RegistrationResponse save(Trainer trainer) {
        User user = trainer.getUser();
        try {
            userService.setPassword(user);
            userService.setUsername(user);
            RegistrationResponse response = userMapper.toRegisterResponse(user);
            user.setActive(true);
            String encodedPassword = passwordEncoder.encode(PasswordMapper.toString(user.getPassword()));
            user.setPassword(PasswordMapper.toCharArray(encodedPassword));
            trainer.setUser(user);
            trainerDAO.save(trainer);
            LOG.info("Added a new trainer: " + trainer);
            return response;
        } catch (Exception ex) {
            LOG.error("Constraint violation: " + ExceptionUtils.getRootCauseMessage(ex));
            throw new ConstraintViolationException(ex.getMessage(), new SQLException(), null);
        }
    }

    @Override
    public void update(Trainer newTrainerInfo) {
        try {
            trainerDAO.update(newTrainerInfo);
            LOG.info("Updated trainee info for ID: " + newTrainerInfo.getId());
        } catch (Exception e) {
                LOG.error("Constraint violation: " + ExceptionUtils.getRootCauseMessage(e));
                throw new ConstraintViolationException(e.getMessage(), new SQLException(), null);
            }
        }

    @Override
    public Trainer getByUsernameAndPassword(String username, String password) {
        Trainer trainer = getByUserName(username);
        String hashedPassword = PasswordMapper.toString(trainer.getUser().getPassword());
        LOG.info("Checking the password matching....");
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new PasswordMatchingException("Wrong password....Try again");
        }
         LOG.info("Password matches successfully!");
        return trainer;
    }
}
