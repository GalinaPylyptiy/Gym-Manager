package com.epam.gym.dao.impl;

import com.epam.gym.dao.TrainerDAO;
import com.epam.gym.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.RollbackException;
import org.springframework.stereotype.Repository;

import java.util.Collection;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.NoResultException;
//import javax.persistence.NonUniqueResultException;
//import javax.persistence.PersistenceUnit;
//import javax.persistence.RollbackException;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public Trainer findByUserName(String userName) throws NoResultException, NonUniqueResultException {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainer trainer = entityManager.createQuery("Select t from Trainer t left join fetch t.trainees inner join t.user u where u.userName = : userName", Trainer.class)
                    .setParameter("userName", userName).getSingleResult();
            entityManager.getTransaction().commit();
            return trainer;
        } catch (NoResultException | NonUniqueResultException ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Trainer with username " + userName + " is not found.");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Trainer findByUsernameAndPassword(String username, String password) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainer trainer = entityManager.createQuery("Select t from Trainer t inner join t.user u " +
                    "where u.userName = : username and u.password = :password", Trainer.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return trainer;
        } catch (NoResultException | NonUniqueResultException ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Trainer with username " + username + " is not found.");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Trainer> getFreeActiveTrainers() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Collection<Trainer> trainers = entityManager.createQuery("SELECT t FROM Trainer t LEFT JOIN t.trainees tr where tr is NULL", Trainer.class)
                    .getResultList();
            entityManager.getTransaction().commit();
            return trainers;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException(ex.getCause());
        }
    }

    @Override
    public Collection<Trainer> getAll() {
        EntityManager entityManager = emf.createEntityManager();
        Collection<Trainer> trainers = entityManager.createQuery("select t from Trainer t", Trainer.class)
                .getResultList();
        entityManager.close();
        return trainers;
    }

    @Override
    public void save(Trainer trainer) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(trainer);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error persisting trainer " + trainer, ex.getCause());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Trainer newTrainerInfo) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(newTrainerInfo);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error update trainer with id " + newTrainerInfo.getId(), ex.getCause());
        } finally {
            entityManager.close();
        }
    }
}

