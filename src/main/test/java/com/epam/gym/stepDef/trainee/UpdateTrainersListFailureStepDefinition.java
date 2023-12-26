package com.epam.gym.stepDef.trainee;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeUpdateTrainersListRequest;
import com.epam.gym.dto.trainer.TrainerDTO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateTrainersListFailureStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    private RegistrationResponse traineeRegistrationResponse;

    private HttpEntity<TraineeUpdateTrainersListRequest> requestEntity;

    private HttpClientErrorException.BadRequest badRequestException;


    @Given("a user sends the put request to the specified url to update the Trainee`s trainers list with empty trainers list")
    public void a_user_sends_the_put_request_with_empty_trainers_list() {
        addTrainee();
        String login = traineeRegistrationResponse.getLogin();
        String password = traineeRegistrationResponse.getPassword();
        String jwt = getToken(login, password);
        requestEntity = new HttpEntity<>(trainersListUpdateRequest(login), getHeaders(jwt));
    }

    @When("the system receives a request and throws an HttpClientErrorException.BadRequest exception")
    public void the_system_receives_the_request() {
        String url = "http://localhost:" + port + "/trainees/updateTrainers";
        try {
            restTemplate.exchange(url,
                    HttpMethod.PUT,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with Bad Request status {}")
    public void return_trainee_profile_response(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private HttpHeaders getHeaders(String token) {
        String auth = "Authorization";
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, "Bearer " + token);
        return headers;
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Jim");
        user.setLastName("Dale");
        trainee.setUser(user);
        Date dateOfBirth = new Date(91, Calendar.MARCH, 31);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Forest St,4");
        traineeRegistrationResponse = traineeService.save(trainee);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private TraineeUpdateTrainersListRequest trainersListUpdateRequest(String traineeUsername) {
        TraineeUpdateTrainersListRequest updateTrainersListRequest = new TraineeUpdateTrainersListRequest();
        updateTrainersListRequest.setTraineeUsername(traineeUsername);
        updateTrainersListRequest.setTrainersUsernameList(List.of());
        return updateTrainersListRequest;
    }
}
