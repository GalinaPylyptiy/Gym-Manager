package stepDef.training;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.training.TrainingCreateDTO;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddTrainingFailureStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private RegistrationResponse registrationResponse;

    private HttpEntity<TrainingCreateDTO> requestEntity;

    private HttpClientErrorException.BadRequest badRequestException;

    @Given("a user sends the post request to the specified url to add new training to the system but missed to insert trainer username")
    public void add_training_with_invalid_data() {
        addTrainee();
        String traineeUsername = registrationResponse.getLogin();
        String traineePassword = registrationResponse.getPassword();
        String jwt = getToken(traineeUsername, traineePassword);
        requestEntity = new HttpEntity<>(trainingCreateDto(),
                getHeaders(jwt));
    }

    @When("the system receives the request and validates data the exception is thrown due to bad request data")
    public void receive_the_request_and_validate_and_throws_an_exception() {
        try {
            String url = "http://localhost:" + port + "/trainings";
            restTemplate.exchange(url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the status {int} - Bad Request is returned to the user")
    public void return_status_400_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private TrainingCreateDTO trainingCreateDto() {
        TrainingCreateDTO trainingCreateDTO = new TrainingCreateDTO();
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);
        Date dateAndTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        trainingCreateDTO.setTraineeUsername(registrationResponse.getLogin());
        trainingCreateDTO.setTrainingName("Group training");
        trainingCreateDTO.setTrainingDate(dateAndTime);
        trainingCreateDTO.setDuration(90);
        return trainingCreateDTO;
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        Date dateOfBirth = new Date(100, Calendar.JULY, 1);
        trainee.setDateOfBirth(dateOfBirth);
        User user = new User();
        user.setFirstName("William");
        user.setLastName("Defoe");
        trainee.setUser(user);
        registrationResponse = traineeService.save(trainee);
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
