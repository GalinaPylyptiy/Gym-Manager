package com.epam.gym.service;


import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.model.User;

public interface UserService {

    void setUsername(User user);
    void setPassword(User user);
    JwtResponseDTO login(String username, String password);
    User getByUsername(String username);
    void changePassword(User user, String newPassword);
    void activateProfile(User user);
    void deactivateProfile(User user);
    User getByLoginAndPassword(String username, String password);

}
