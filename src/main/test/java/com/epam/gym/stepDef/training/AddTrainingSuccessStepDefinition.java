package com.epam.gym.stepDef.training;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.training.TrainingCreateDTO;
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

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTrainingSuccessStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private RegistrationResponse traineeRegistrationResponse;
    private RegistrationResponse trainerRegistrationResponse;
    private HttpEntity<TrainingCreateDTO> requestEntity;
    private ResponseEntity<String> responseEntity;

    @Given("a user wants to add a new training to the system and insert valid data")
    public void add_training_with_valid_data(){
        addTrainer();
        addTrainee();
        String traineeUsername = traineeRegistrationResponse.getLogin();
        String trainerUsername = trainerRegistrationResponse.getLogin();
        String trainerPassword = trainerRegistrationResponse.getPassword();
        String jwt = getToken(trainerUsername, trainerPassword);
        requestEntity = new HttpEntity<>(trainingCreateDto(traineeUsername, trainerUsername),
                getHeaders(jwt));

    }

    @When("the system receives the post request and validates data then is saves new training to the system")
    public void receive_the_request_and_validate_and_saves_training_to_the_system(){
        String url = "http://localhost:" + port + "/trainings";
        responseEntity = restTemplate.exchange(url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    @Then("the operation was a success and the status {int} is returned")
    public void return_status_200_ok(int expected){
        assertEquals(HttpStatus.valueOf(expected), responseEntity.getStatusCode());
    }

    private void addTrainer(){
        Trainer trainer = new Trainer();
        TrainingType specialization = getTrainingType();
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        trainerRegistrationResponse =  trainerService.save(trainer);
    }

    private TrainingCreateDTO trainingCreateDto(String traineeUsername, String trainerUsername){
        TrainingCreateDTO trainingCreateDTO = new TrainingCreateDTO();
        Date trainingTime = new Date(124, Calendar.JANUARY, 1);
        trainingCreateDTO.setTrainerUsername(trainerUsername);
        trainingCreateDTO.setTraineeUsername(traineeUsername);
        trainingCreateDTO.setTrainingName("Group training");
        trainingCreateDTO.setTrainingDate(trainingTime);
        trainingCreateDTO.setDuration(90);
        return trainingCreateDTO;
    }

    private TrainingType getTrainingType(){
        return trainingTypeService.findById(2L);
    }

    private void addTrainee(){
        Trainee trainee = new Trainee();
        Date dateOfBirth = new Date(100, Calendar.JULY, 1);
        trainee.setDateOfBirth(dateOfBirth);
        User user = new User();
        user.setFirstName("William");
        user.setLastName("Defoe");
        trainee.setUser(user);
        traineeRegistrationResponse = traineeService.save(trainee);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private HttpHeaders getHeaders(String token) {
        String auth = "Authorization";
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, "Bearer " + token);
        return headers;
    }

}
