package com.epam.gym.mapper;

import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.model.User;
import com.epam.gym.util.PasswordMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RegistrationResponse toRegisterResponse(User user){
        RegistrationResponse response = new RegistrationResponse();
        response.setLogin(user.getUserName());
        response.setPassword(PasswordMapper.toString(user.getPassword()));
        return response;
    }
}
