package com.epam.gym.service.impl;

import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.dto.training.ActionType;
import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.message.TrainingReportMessageSender;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.service.TrainingService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service(value = "trainingService")
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainingMapper trainingMapper;
    private final TrainingReportMessageSender messageSender;
    private static Log LOG = LogFactory.getLog(TrainingServiceImpl.class);

    public TrainingServiceImpl(TrainingDAO trainingDAO,
                               TrainingMapper trainingMapper,
                               TrainingReportMessageSender messageSender) {
        this.trainingDAO = trainingDAO;
        this.trainingMapper = trainingMapper;
        this.messageSender = messageSender;
    }

    @Override
    public Collection<Training> getAll() {
        return trainingDAO.getAll();
    }

    @Override
    @Transactional
    public void save(Training training) {
        Trainer trainer = training.getTrainer();
        Trainee trainee = training.getTrainee();
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        trainingDAO.save(training);
        messageSender.send(getTrainingRequest(ActionType.ADD, training));
        LOG.info("Added new training " + training);
    }

    @Override
    @Transactional
    public void delete(Training training) {
        Trainer trainer = training.getTrainer();
        Trainee trainee = training.getTrainee();
        trainee.getTrainers().remove(trainer);
        trainingDAO.delete(training.getId());
        LOG.info("Deleted training with id " + training.getId());
        messageSender.send(getTrainingRequest(ActionType.DELETE, training));
    }

    @Override
    public Training getById(Long id) {
        try {
            LOG.info("Getting training by its id....");
            return trainingDAO.getById(id);
        } catch (Exception ex) {
            LOG.error("Error getting training by id " + id, ex.getCause());
            throw new EntityNotFoundException("Error getting training by id " + id);
        }
    }

    private TrainingReportRequest getTrainingRequest(ActionType actionType, Training training) {
        TrainingReportRequest clientRequest = trainingMapper.toRequestDtoWithoutActionType(training);
        clientRequest.setActionType(actionType);
        return clientRequest;
    }
}
