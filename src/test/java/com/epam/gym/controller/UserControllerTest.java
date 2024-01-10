package com.epam.gym.controller;

import com.epam.gym.dto.ChangePasswordRequest;
import com.epam.gym.model.User;
import com.epam.gym.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testChangePassword() throws Exception {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String username = "username";
        User user = new User();
        user.setUserName(username);
        user.setPassword(oldPassword.toCharArray());
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setUsername(username);

        when(userService.getByLoginAndPassword(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeProfileActivityActivate() throws Exception {
        User user = new User();
        doReturn(user).when(userService).getByLoginAndPassword("validUsername", "password");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/changeProfileActivity")
                .content("{\"username\":\"validUsername\",\"password\":\"password\",\"active\":true}")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testChangeProfileActivityDeactivate() throws Exception {
        User user = new User();
        doReturn(user).when(userService).getByLoginAndPassword("validUsername", "password");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/changeProfileActivity")
                .content("{\"username\":\"validUsername\",\"password\":\"password\",\"active\":false}")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}