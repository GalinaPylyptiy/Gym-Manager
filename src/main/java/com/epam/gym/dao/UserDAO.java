package com.epam.gym.dao;


import com.epam.gym.model.User;

public interface UserDAO {

    User findByUsername(String username);
//    User getByLoginAndPassword(String username, String password);
    void activate(User user);
    void deactivate(User user);
    void changePassword(User user, String newPassword);
    boolean isUserExists(User user);
    int getSerialNumberCount(String username);
    boolean isUserNameExists(String username);

}
