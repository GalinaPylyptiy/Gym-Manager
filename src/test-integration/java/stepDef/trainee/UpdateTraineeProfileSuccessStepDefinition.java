package stepDef.trainee;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateTraineeProfileSuccessStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    private RegistrationResponse registrationResponse;

    private ResponseEntity<TraineeUpdateResponse> traineeUpdateResponseResponseEntity;

    @Given("a user sends the request to the specified url with TraineeUpdateRequest dto")
    public void a_user_sends_the_get_request_to_the_specified_url_with_username_and_password() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        String url = "http://localhost:" + port + "/trainees/" + login;
        HttpEntity<TraineeUpdateRequest> requestEntity = new HttpEntity<>(traineeUpdateRequest(), getHeaders(jwt));
        traineeUpdateResponseResponseEntity = restTemplate.exchange(url,
                HttpMethod.PUT,
                requestEntity,
                TraineeUpdateResponse.class
        );
    }

    @When("the system receives a request to update the trainee info and return the status {int}")
    public void the_system_receives_the_request(int expected) {
        assertEquals(HttpStatus.valueOf(expected), traineeUpdateResponseResponseEntity.getStatusCode());
    }

    @Then("the system should respond with a successful trainee update response")
    public void return_trainee_profile_response() {
        TraineeUpdateResponse traineeUpdateResponse = traineeUpdateResponseResponseEntity.getBody();
        assertNotNull(traineeUpdateResponse);
        assertEquals("Harry", traineeUpdateResponse.getFirstName());
        assertEquals("Potter", traineeUpdateResponse.getLastName());
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
        registrationResponse = traineeService.save(trainee);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

    private TraineeUpdateRequest traineeUpdateRequest() {
        TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest();
        Random random = new Random();
        traineeUpdateRequest.setFirstName("Harry");
        traineeUpdateRequest.setLastName("Potter");
        traineeUpdateRequest.setPassword(PasswordGenerator.generateRandomPassword(10));
        traineeUpdateRequest.setUsername("Harry_Potter_"+ random.nextInt(100));
        traineeUpdateRequest.setActive(true);
        Date dateOfBirth = new Date(80, Calendar.JULY, 31);
        traineeUpdateRequest.setDateOfBirth(dateOfBirth);
        traineeUpdateRequest.setAddress("Privet Drive,4");
        return traineeUpdateRequest;
    }

}
