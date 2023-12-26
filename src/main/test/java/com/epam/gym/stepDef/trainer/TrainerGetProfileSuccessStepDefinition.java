package com.epam.gym.stepDef.trainer;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.dto.trainer.TrainerProfileResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainerGetProfileSuccessStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private UserService userService;

    private RegistrationResponse registrationResponse;

    private ResponseEntity<TrainerProfileResponse> trainerProfileResponseResponseEntity;

    @Given("a user sends the get request to the specified url for trainer getting profile with valid username and password")
    public void a_user_sends_the_get_request_to_the_url_with_username_and_password_to_get_trainer_profile() {
        addTrainer();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        String url = "http://localhost:" + port + "/trainers";
        HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders(jwt));
        String urlWithParams = UriComponentsBuilder.fromUriString(url)
                .queryParam("username", login)
                .queryParam("password", password)
                .toUriString();
        trainerProfileResponseResponseEntity = restTemplate.exchange(urlWithParams,
                HttpMethod.GET,
                requestEntity,
                TrainerProfileResponse.class
        );
    }

    @When("the system checks the credentials and retrieving trainer information and returns status code {int}")
    public void the_system_receives_the_request_and_returns_status_ok(int expected) {
        assertEquals(HttpStatus.valueOf(expected), trainerProfileResponseResponseEntity.getStatusCode());
    }

    @Then("the system should respond with TrainerProfileResponse generated by the system")
    public void returns_trainer_profile_response() {
        TrainerProfileResponse trainerProfileResponse = trainerProfileResponseResponseEntity.getBody();
        assertNotNull(trainerProfileResponse);
        assertNotNull(trainerProfileResponse.getFirstName());
        assertNotNull(trainerProfileResponse.getLastName());
    }

    private HttpHeaders getHeaders(String token) {
        String auth = "Authorization";
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, "Bearer " + token);
        return headers;
    }

    private void addTrainer(){
        Trainer trainer = new Trainer();
        TrainingType specialization = getTrainingType();
        User user = new User();
        user.setFirstName("Irina");
        user.setLastName("Petrova");
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        registrationResponse = trainerService.save(trainer);
    }

    private TrainingType getTrainingType(){
        return trainingTypeService.findById(2L);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

}