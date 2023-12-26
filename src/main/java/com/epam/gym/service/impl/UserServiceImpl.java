package com.epam.gym.service.impl;

import com.epam.gym.dao.UserDAO;
import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.exception.AuthenticationFailedException;
import com.epam.gym.exception.ExceedAttemptsLimitException;
import com.epam.gym.exception.PasswordMatchingException;
import com.epam.gym.listener.AuthenticationFailureListener;
import com.epam.gym.model.User;
import com.epam.gym.securityService.JwtService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.PasswordMapper;
import com.epam.gym.util.UserNameGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final AuthenticationFailureListener failureListener;
    private final static Log LOG = LogFactory.getLog(UserServiceImpl.class);

    public UserServiceImpl(UserDAO userDAO, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authManager, AuthenticationFailureListener failureListener) {
        this.userDAO = userDAO;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.failureListener = failureListener;
    }

    @Override
    public void setUsername(User user) {
        String firstName = Objects.requireNonNull(user.getFirstName());
        String lastName = Objects.requireNonNull(user.getLastName());
        String userName = UserNameGenerator.generateUserName(firstName, lastName);
        LOG.info("Setting username for user: " + user.getFirstName() + " " + user.getLastName());
        if (isUserNameExists(userName)) {
            int serial = userDAO.getSerialNumberCount(userName);
            user.setUserName(userName + "_" + ++serial);
        } else {
            user.setUserName(userName);
        }
    }

    @Override
    public void setPassword(User user) {
        int passwordLength = 10;
        LOG.info("Setting password for user: " + user.getFirstName() + " " + user.getLastName());
        String password = PasswordGenerator.generateRandomPassword(passwordLength);
        user.setPassword(PasswordMapper.toCharArray(password));
    }

    @Override
    public JwtResponseDTO login(String username, String password) {
        checkMaxFailedLoginCounts(username);
        try {
            return authenticate(username, password);
        } catch (AuthenticationException ex) {
            LOG.error("Authentication for " + username + " failed. Bad credentials");
            throw new AuthenticationFailedException("Authentication for " + username + " failed, " + ex.getLocalizedMessage());
        }
    }

    private JwtResponseDTO authenticate(String username, String password) {
        LOG.info("Authentication for  " + username + " started...");
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        LOG.info("User with username " + username + " is authenticated successfully");
        String jwtToken = jwtService.generateToken(username);
        MDC.put("Authorization", jwtToken);
        JwtResponseDTO responseDTO = new JwtResponseDTO();
        responseDTO.setJwt(jwtToken);
        return responseDTO;
    }

    private void checkMaxFailedLoginCounts(String username) {
        if (failureListener.isBlocked(username)) {
            LOG.error("User is blocked due to failure logging attempts");
            throw new ExceedAttemptsLimitException("You exceed your attempts limit. You can proceed you authentication in  "
                    + (failureListener.extractMinutesLeft(username)+1) + " minutes.");
        }
    }

    @Override
    public User getByUsername(String username) {
        try {
            return userDAO.findByUsername(username);
        } catch (EntityNotFoundException ex) {
            LOG.error("User with username " + username + " is not found. "+ ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void changePassword(User user, String newPassword) {
        try {
            String encodedPassword = passwordEncoder.encode(newPassword);
            userDAO.changePassword(user, encodedPassword);
            LOG.info("Updated password for : " + user.getUserName());
        } catch (Exception e) {
            LOG.error("Constraint violation: " + ExceptionUtils.getRootCauseMessage(e));
            throw new ConstraintViolationException(e.getMessage(), new SQLException(), null);
        }
    }

    @Override
    public void activateProfile(User user) {
            userDAO.activate(user);
            LOG.info("Profile: " + user.getUserName() + " is activated. ");
    }

    @Override
    public void deactivateProfile(User user) {
            userDAO.deactivate(user);
            LOG.info("Profile: " + user.getUserName() + " is deactivated. ");
    }

    @Override
    public User getByLoginAndPassword(String username, String password) {
        User user = getByUserName(username);
        String hashedPassword = PasswordMapper.toString(user.getPassword());
        LOG.info("Checking the password matching....");
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new PasswordMatchingException("Wrong password....Try again");
        }
        LOG.info("Password matches successfully!");
        return user;
    }

    private User getByUserName(String username) {
        return userDAO.findByUsername(username);
    }

    private boolean isUserNameExists(String userName) {
        return userDAO.isUserNameExists(userName);
    }

}
