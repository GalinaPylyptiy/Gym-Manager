package stepDef.trainee;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.gym.dto.trainee.TraineeUpdateResponse;
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

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateTraineeProfileFailureStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    private RegistrationResponse registrationResponse;

    private HttpClientErrorException.BadRequest badRequestException;

    private HttpEntity<TraineeUpdateRequest> requestEntity;


    @Given("a user sends the put request to the specified url to update the trainee profile with the invalid data")
    public void a_user_sends_the_put_request_with_trainee_update_info_with_invalid_data() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        requestEntity = new HttpEntity<>(traineeUpdateRequest(), getHeaders(jwt));
    }

    @When("the system receives a request to update the trainee profile and returns the status {int}")
    public void the_system_receives_the_request_with_invalid_data_and_throws_an_exception(int expected) {
        String login = registrationResponse.getLogin();
        String url = "http://localhost:" + port + "/trainees/" + login;
        try {
            restTemplate.exchange(url,
                    HttpMethod.PUT,
                    requestEntity,
                    TraineeUpdateResponse.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with status {int} bad request")
    public void return_bad_request_status_code_400(int expected) {
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
        traineeUpdateRequest.setFirstName("Harry");
        traineeUpdateRequest.setLastName("");
        traineeUpdateRequest.setPassword("");
        traineeUpdateRequest.setUsername("scar_face");
        traineeUpdateRequest.setActive(true);
        Date dateOfBirth = new Date(80, Calendar.JULY, 31);
        traineeUpdateRequest.setDateOfBirth(dateOfBirth);
        traineeUpdateRequest.setAddress("Privet Drive,4");
        return traineeUpdateRequest;
    }

}
