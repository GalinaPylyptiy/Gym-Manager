package com.epam.gym.dao.impl;

import com.epam.gym.dao.TrainingDAO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public Training getById(Long id) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.find(Training.class, id);
        }
    }

    @Override
    public Collection<Training> getAll() {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Training> query = entityManager.createQuery("select t from Training t", Training.class);
        Collection<Training> trainings = query.getResultList();
        entityManager.close();
        return trainings;
    }

    @Override
    public void save(Training training) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(training);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error persisting training" + training, ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Training newTrainingInfo) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(newTrainingInfo);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error update training with id " + newTrainingInfo.getId(), ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Long trainingId) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            Training training = entityManager.createQuery("SELECT t FROM Training t WHERE t.id = :id", Training.class)
                    .setParameter("id", trainingId).getSingleResult();
            entityManager.remove(training);
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error deleting training with id " + trainingId, ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Training> getTraineeTrainings(String traineeUserName) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> training = query.from(Training.class);
            Join<Training, Trainee> traineeJoin = training.join("trainee", JoinType.INNER);
            Join<Trainee, User> userJoin = traineeJoin.join("user", JoinType.INNER);
            query.where(cb.equal(userJoin.get("userName"), traineeUserName));
            Collection<Training> traineeTrainings =  entityManager.createQuery(query).getResultList();
            entityManager.getTransaction().commit();
            return traineeTrainings;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error fetching trainings of a trainee " + traineeUserName , ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Training> getTrainerTrainings(String trainerUserName) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> training = query.from(Training.class);
            Join<Training, Trainer> trainerJoin = training.join("trainer", JoinType.INNER);
            Join<Trainee, User> userJoin = trainerJoin.join("user", JoinType.INNER);
            query.where(cb.equal(userJoin.get("userName"), trainerUserName));
            Collection<Training> trainerTrainings =  entityManager.createQuery(query).getResultList();
            entityManager.getTransaction().commit();
            return trainerTrainings;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error fetching trainings of a trainer " + trainerUserName, ex.getCause());
        } finally {
            entityManager.close();
        }
    }
}
