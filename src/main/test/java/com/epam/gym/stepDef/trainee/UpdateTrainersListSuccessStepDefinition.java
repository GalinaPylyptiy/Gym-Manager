package com.epam.gym.stepDef.trainee;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
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
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateTrainersListSuccessStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private UserService userService;

    private RegistrationResponse traineeRegistrationResponse;

    private RegistrationResponse trainerRegistrationResponse;

    private ResponseEntity<List<TrainerDTO>> trainersListResponseEntity;

    @Given("a user sends the request to the specified url to update the Trainee`s trainers list with the Trainee username and list of trainers usernames")
    public void a_user_sends_the_get_request_to_the_specified_url_with_username_and_password() {
        addTrainee();
        addTrainer();
        String login = traineeRegistrationResponse.getLogin();
        String password = traineeRegistrationResponse.getPassword();
        String jwt = getToken(login, password);
        String url = "http://localhost:" + port + "/trainees/updateTrainers";
        HttpEntity<TraineeUpdateTrainersListRequest> requestEntity = new HttpEntity<>(trainersListUpdateRequest(login), getHeaders(jwt));
        trainersListResponseEntity = restTemplate.exchange(url,
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @When("the system receives a request to update the trainee's trainers list and return the status {int}")
    public void the_system_receives_the_request(int expected) {
        assertEquals(HttpStatus.valueOf(expected), trainersListResponseEntity.getStatusCode());
    }

    @Then("the system should respond with a list of updated trainers")
    public void return_trainee_profile_response() {
        List<TrainerDTO> trainersList = trainersListResponseEntity.getBody();
        assertNotNull(trainersList);
        assertFalse(trainersList.isEmpty());
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
        user.setFirstName("Lorry");
        user.setLastName("Totter");
        trainee.setUser(user);
        Date dateOfBirth = new Date(80, Calendar.JULY, 31);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Privet Drive,4");
        traineeRegistrationResponse = traineeService.save(trainee);
    }

    private void addTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Tom");
        user.setLastName("Hardy");
        trainer.setUser(user);
        TrainingType trainingType = trainingTypeService.findById(3L);
        trainer.setSpecialization(trainingType);
        trainerRegistrationResponse = trainerService.save(trainer);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private TraineeUpdateTrainersListRequest trainersListUpdateRequest(String traineeUsername) {
        TraineeUpdateTrainersListRequest updateTrainersListRequest = new TraineeUpdateTrainersListRequest();
        updateTrainersListRequest.setTraineeUsername(traineeUsername);
        updateTrainersListRequest.setTrainersUsernameList(List.of(trainerRegistrationResponse.getLogin()));
        return updateTrainersListRequest;
    }
}
