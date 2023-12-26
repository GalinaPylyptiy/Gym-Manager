package com.epam.gym.stepDef.trainer;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.gym.dto.trainer.TrainerUpdateResponse;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateTrainerProfileSuccessStepDefinition {

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

    private ResponseEntity<TrainerUpdateResponse> trainerUpdateResponseResponseEntity;

    @Given("a user sends the put request to the specified url with TrainerUpdateRequest")
    public void a_user_sends_the_get_request_to_the_system_with_trainer_update_request() {
        addTrainer();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        String url = "http://localhost:" + port + "/trainers/" + login;
        HttpEntity<TrainerUpdateRequest> requestEntity = new HttpEntity<>(trainerUpdateRequest(), getHeaders(jwt));
        trainerUpdateResponseResponseEntity = restTemplate.exchange(url,
                HttpMethod.PUT,
                requestEntity,
                TrainerUpdateResponse.class
        );
    }

    @When("the system receives a request to update the trainer profile and returns the status {int}")
    public void the_system_receives_the_request(int expected) {
        assertEquals(HttpStatus.valueOf(expected), trainerUpdateResponseResponseEntity.getStatusCode());
    }

    @Then("the system should respond with a TrainerUpdateResponse")
    public void return_trainee_profile_response() {
        TrainerUpdateResponse trainerUpdateResponse = trainerUpdateResponseResponseEntity.getBody();
        assertNotNull(trainerUpdateResponse);
        assertEquals("Sam", trainerUpdateResponse.getFirstName());
        assertEquals("Petrov", trainerUpdateResponse.getLastName());
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
        user.setFirstName("Sam");
        user.setLastName("Ivanov");
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        registrationResponse = trainerService.save(trainer);
    }

    private TrainingType getTrainingType(){
        return trainingTypeService.findById(1L);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private TrainerUpdateRequest trainerUpdateRequest() {
        TrainerUpdateRequest updateRequest = new TrainerUpdateRequest();
        Random random = new Random();
        updateRequest.setFirstName("Sam");
        updateRequest.setLastName("Petrov");
        updateRequest.setPassword(PasswordGenerator.generateRandomPassword(10));
        updateRequest.setUsername("Sam__Petrov"+ random.nextInt(100));
        updateRequest.setActive(true);
        return updateRequest;
    }

}
