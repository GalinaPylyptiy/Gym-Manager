package com.epam.gym.dao.impl;

import com.epam.gym.dao.UserDAO;
import com.epam.gym.model.User;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.RollbackException;

@Service
public class UserDAOImpl implements UserDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;
    private Log log = LogFactory.getLog(UserDAOImpl.class);


    @Override
    public User findByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.userName = :username AND u.isActive= true", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            log.error(ex.getMessage());
            throw new EntityNotFoundException("User with username "+ username + " is not found.");
        }finally {
            entityManager.close();
        }
    }

//    @Override
//    public User getByLoginAndPassword(String username, String password) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :username" +
//                    " AND u.password = :password AND u.isActive= true")
//                    .setParameter("username", username)
//                    .setParameter("password", password)
//                    .getSingleResult();
//        } catch (NoResultException | NonUniqueResultException ex) {
//            log.error(ex.getCause());
//            throw new EntityNotFoundException("User with username "+ username + " is not found.");
//        } finally {
//            entityManager.close();
//        }
//    }

    @Override
    public void activate(User user) {
        changeProfileActivity(user, true);
    }

    @Override
    public void deactivate(User user)  {
        changeProfileActivity(user, false);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("Update User u set u.password = :newPassword where u.password = :oldPassword ")
                    .setParameter("newPassword", newPassword)
                    .setParameter("oldPassword", user.getPassword())
                    .executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            log.error(ex.getCause());
            throw new RollbackException("Error updating password for " + user.getUserName(), ex.fillInStackTrace());
        } finally {
            entityManager.close();
        }
    }

    @Timed
    @Override
    public boolean isUserExists(User user) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            Long result = (Long) entityManager.createQuery("SELECT COUNT (u) FROM User u WHERE u.userName = :username" +
                    " AND u.password = :password")
                    .setParameter("username", user.getUserName())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            return result > 0;
        } catch (Exception ex) {
            log.error(ex.getCause());
            return false;
        }
    }

    @Override
    public int getSerialNumberCount(String username) {
        EntityManager entityManager = emf.createEntityManager();
        String usernamePattern = username + "%";
        Query serialNumberQuery = entityManager.createQuery(
                "SELECT COUNT (u) FROM User u WHERE u.userName LIKE :username");
        serialNumberQuery.setParameter("username", usernamePattern);
        int serialNumberCount = ((Number) serialNumberQuery.getSingleResult()).intValue();
        entityManager.close();
        return serialNumberCount;
    }

    @Override
    public boolean isUserNameExists(String username) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            Long result = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName LIKE :username")
                    .setParameter("username", username)
                    .getSingleResult();
            return result != null && result > 0;
        } catch (NoResultException ex) {
            return false;
        } finally {
            entityManager.close();
        }
    }

    private void changeProfileActivity(User user, boolean isActive) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("Update User u set u.isActive = :isActive where u.id = :userId ")
                    .setParameter("isActive", isActive)
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw new RollbackException("Error in changing profile for " + user.getUserName(), ex.getCause());
        } finally {
            entityManager.close();
        }
    }
}

