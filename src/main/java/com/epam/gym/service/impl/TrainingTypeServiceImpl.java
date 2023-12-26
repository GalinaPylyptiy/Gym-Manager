package com.epam.gym.service.impl;

import com.epam.gym.dao.TrainingTypeDAO;

import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainingTypeService;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;
    private static Log LOG = LogFactory.getLog(TrainingTypeServiceImpl.class);


    public TrainingTypeServiceImpl(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    @Override
    public TrainingType findById(Long id) {
        LOG.info("Finding training type by id " + id);
        return trainingTypeDAO.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Training type with id " + id + " is not found") );
    }

    @Override
    public Collection<TrainingType> getAll() {
        LOG.info("Getting all training types.... ");
        return trainingTypeDAO.getAll();
    }
}
