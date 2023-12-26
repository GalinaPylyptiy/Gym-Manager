package com.epam.gym.stepDef.trainer;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.dto.trainer.TrainerUpdateResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.PasswordGenerator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateTrainerProfileFailureStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private RegistrationResponse registrationResponse;

    private HttpClientErrorException.BadRequest badRequestException;

    private HttpEntity<TrainerUpdateRequest> requestEntity;


    @Given("a user sends the put request to the specified url to update the trainer profile but leave the username blank")
    public void a_user_sends_the_put_request_with_trainer_update_info_with_blank_username() {
        addTrainer();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        requestEntity = new HttpEntity<>(trainerUpdateRequest(), getHeaders(jwt));
    }

    @When("the system receives a request to update the trainer profile and throws an exception for invalid data")
    public void the_system_receives_the_request_with_bad_request_and_throws_an_exception() {
        String login = registrationResponse.getLogin();
        String url = "http://localhost:" + port + "/trainees/" + login;
        try {
            restTemplate.exchange(url,
                    HttpMethod.PUT,
                    requestEntity,
                    TrainerUpdateResponse.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with status {int} bad request for invalid request data to update trainer")
    public void return_bad_request_exception_with_status_code_400(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private HttpHeaders getHeaders(String token) {
        String auth = "Authorization";
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, "Bearer " + token);
        return headers;
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private void addTrainer() {
        Trainer trainer = new Trainer();
        TrainingType specialization = getTrainingType();
        User user = new User();
        user.setFirstName("Ilya");
        user.setLastName("Sobolev");
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        registrationResponse = trainerService.save(trainer);
    }

    private TrainingType getTrainingType() {
        return trainingTypeService.findById(4L);
    }

    private TrainerUpdateRequest trainerUpdateRequest() {
        TrainerUpdateRequest updateRequest = new TrainerUpdateRequest();
        updateRequest.setFirstName("Ilya");
        updateRequest.setLastName("Petrov");
        updateRequest.setPassword(PasswordGenerator.generateRandomPassword(10));
        updateRequest.setUsername("");
        updateRequest.setActive(true);
        return updateRequest;
    }

}
