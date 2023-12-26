package com.epam.gym.controller;

import com.epam.gym.dto.AuthenticationDTO;
import com.epam.gym.dto.ChangeActivityRequest;
import com.epam.gym.dto.ChangePasswordRequest;
import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.model.User;
import com.epam.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    @Operation(summary = "Login by entering username and password")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDTO login(@RequestBody AuthenticationDTO authentication) {
        return userService.login(authentication.getUsername(), authentication.getPassword());
    }

    @PatchMapping("/changePassword")
    @Operation(summary = "Change password by entering username, old password and new password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request){
        User user = userService.getByLoginAndPassword(request.getUsername(), request.getOldPassword());
        userService.changePassword(user, request.getNewPassword());
    }

    @PatchMapping("/changeProfileActivity")
    @Operation(summary = "Activate/deactivate profile activity by entering username, password and boolean value")
    @ResponseStatus(HttpStatus.OK)
    public void changeActivity(@Valid @RequestBody ChangeActivityRequest request){
        User user = userService.getByLoginAndPassword(request.getUsername(), request.getPassword());
        if (request.isActive()){
            userService.activateProfile(user);
        } else {
            userService.deactivateProfile(user);
        }
    }
}
