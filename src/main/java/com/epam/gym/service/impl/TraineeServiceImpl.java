package com.epam.gym.service.impl;

import com.epam.gym.client.TrainingReportClient;
import com.epam.gym.dao.TraineeDAO;
import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.training.ActionType;
import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.exception.PasswordMatchingException;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.mapper.UserMapper;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.PasswordMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "traineeService")
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;
    private final TrainingDAO trainingDAO;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingReportClient trainingReportClient;
    private final static Log LOG = LogFactory.getLog(TraineeServiceImpl.class);

    public TraineeServiceImpl(TraineeDAO traineeDAO,
                              TrainingDAO trainingDAO,
                              PasswordEncoder passwordEncoder,
                              UserService userService, UserMapper userMapper,
                              @Lazy TrainingMapper trainingMapper,
                              TrainingReportClient trainingReportClient) {
        this.traineeDAO = traineeDAO;
        this.trainingDAO = trainingDAO;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userMapper = userMapper;
        this.trainingMapper = trainingMapper;
        this.trainingReportClient = trainingReportClient;
    }

    @Override
    public Trainee getByUserName(String userName) {
        try {
            LOG.info("Getting trainee by username " + userName);
            return traineeDAO.findByUserName(userName);
        } catch (RollbackException e) {
            LOG.error(e.getMessage());
            throw new EntityNotFoundException("The trainee with username " + userName + " does not exist", e);
        }
    }

    @Override
    public Trainee getByUsernameAndPassword(String username, String password) {
        Trainee trainee = getByUserName(username);
        String hashedPassword = PasswordMapper.toString(trainee.getUser().getPassword());
        LOG.info("Checking the password matching....");
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new PasswordMatchingException("Wrong password....Try again");
        }
        LOG.info("Password matches successfully!");
        return trainee;
    }

    @Override
    public RegistrationResponse save(Trainee trainee) {
        User user = trainee.getUser();
        try {
            userService.setPassword(user);
            userService.setUsername(user);
            RegistrationResponse response = userMapper.toRegisterResponse(user);
            user.setActive(true);
            String encodedPassword = passwordEncoder.encode(PasswordMapper.toString(user.getPassword()));
            user.setPassword(PasswordMapper.toCharArray(encodedPassword));
            trainee.setUser(user);
            traineeDAO.save(trainee);
            LOG.info("Added a new trainee: " + trainee);
            return response;
        } catch (Exception ex) {
            LOG.error("Constraint violation: " + ExceptionUtils.getRootCauseMessage(ex));
            throw new ConstraintViolationException(ex.getMessage(), new SQLException(), null);
        }
    }

    @Override
    public void assignTrainers(Trainee trainee, List<Trainer> trainers) {
        trainee.setTrainers(trainers);
        traineeDAO.update(trainee);
        LOG.info("New trainers assigned for trainee " + trainee.getId());
    }

    @Override
    public Collection<Trainee> getAll() {
        LOG.info("Getting all trainees...");
        return traineeDAO.getAll();
    }

    @Override
    public void delete(Trainee trainee) {
        String username = trainee.getUser().getUserName();
        List<Training> trainings = (List<Training>) trainingDAO.getTraineeTrainings(trainee.getUser().getUserName());
        List<Training> futureTrainings = trainings.stream()
                .filter(training -> training.getTrainingTime().after(new Date()))
                .collect(Collectors.toList());
        futureTrainings.forEach(training -> {
            TrainingReportRequest request = trainingMapper.toRequestDtoWithoutActionType(training);
            request.setActionType(ActionType.DELETE);
            trainingReportClient.postTraining(request);
        });
        trainings.removeAll(futureTrainings);
        traineeDAO.deleteByUserName(username);
        LOG.info("Deleted trainee with username: " + username);
    }

    @Override
    public void update(Trainee newTraineeInfo) {
        try {
            traineeDAO.update(newTraineeInfo);
            LOG.info("Updated trainee info for ID: " + newTraineeInfo.getId());
        } catch (Exception e) {
            LOG.error("Constraint violation: " + ExceptionUtils.getRootCauseMessage(e));
            throw new ConstraintViolationException(e.getMessage(), new SQLException(), null);
        }
    }

    @Override
    public Collection<Training> getAllTrainings(String userName) {
        LOG.info("Getting all trainee`s trainings by username: " + userName);
        return trainingDAO.getTraineeTrainings(userName);
    }

    @Override
    public void updateTrainersList(Trainee trainee, List<Trainer> newTrainersList) {
        trainee.setTrainers(newTrainersList);
        traineeDAO.update(trainee);
        LOG.info("Updated trainers list for : " + trainee.getUser().getUserName());
    }
}
