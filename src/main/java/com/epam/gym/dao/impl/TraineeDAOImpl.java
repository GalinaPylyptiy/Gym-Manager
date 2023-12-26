package com.epam.gym.dao.impl;

import com.epam.gym.dao.TraineeDAO;
import com.epam.gym.model.Trainee;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.RollbackException;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public Trainee findByUserName(String userName) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainee trainee = entityManager.createQuery("SELECT t FROM Trainee t JOIN t.user u WHERE u.userName = :userName", Trainee.class)
                    .setParameter("userName", userName).getSingleResult();
            entityManager.getTransaction().commit();
            return trainee;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Trainee with username " + userName + " is not found.", ex);
        } finally {
            entityManager.close();
        }
    }

    @Timed
    @Override
    public Trainee findByUsernameAndPassword(String username, String password) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainee trainee = entityManager.createQuery("SELECT t FROM Trainee t JOIN t.user u " +
                    "WHERE u.userName = :username AND u.password = :password", Trainee.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return trainee;
        } catch (NoResultException | NonUniqueResultException ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Trainee with username " + username + " is not found.", ex);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void save(Trainee trainee) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error persisting trainee" + trainee, ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteByUserName(String userName) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainee trainee = entityManager.createQuery("SELECT t FROM Trainee t JOIN t.user u WHERE u.userName = : userName", Trainee.class)
                    .setParameter("userName", userName).getSingleResult();
            entityManager.remove(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error deleting trainee with username: " + userName, ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Trainee newTraineeInfo) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(newTraineeInfo);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error update trainee with id " + newTraineeInfo.getId(), ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Trainee> getAll() {
        EntityManager entityManager = emf.createEntityManager();
        Collection<Trainee> traineeList = entityManager.createQuery("Select t from Trainee t ", Trainee.class)
                .getResultList();
        entityManager.close();
        return traineeList;
    }

}
