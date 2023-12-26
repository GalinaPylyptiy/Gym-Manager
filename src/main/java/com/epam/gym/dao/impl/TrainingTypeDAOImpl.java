package com.epam.gym.dao.impl;

import com.epam.gym.dao.TrainingTypeDAO;
import com.epam.gym.model.TrainingType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    @Override
    public Collection<TrainingType> getAll() {
        TypedQuery<TrainingType> query = entityManager.createQuery("Select t from TrainingType t", TrainingType.class);
        return query.getResultList();
    }
}
