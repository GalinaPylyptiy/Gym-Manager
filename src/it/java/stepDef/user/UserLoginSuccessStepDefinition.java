package stepDef.user;

import com.epam.gym.dto.AuthenticationDTO;
import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.User;
import com.epam.gym.service.TraineeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserLoginSuccessStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TraineeService traineeService;
    @LocalServerPort
    private String port;
    private AuthenticationDTO authenticationDTO;
    private ResponseEntity<JwtResponseDTO> responseEntity;
    private RegistrationResponse registrationResponse;
    private HttpEntity<AuthenticationDTO> requestEntity;


    @Given("a user sends a post request to the specific url using valid login and password")
    public void a_user_send_the_post_request_to_the_url_for_trainer_register() {
        addTrainee();
        authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(registrationResponse.getLogin());
        authenticationDTO.setPassword(registrationResponse.getPassword());
        requestEntity = new HttpEntity<>(authenticationDTO);

    }

    @When("a system receives the request and confirms that the credentials are valid")
    public void the_system_receives_request_and_checks_data() {
        responseEntity = restTemplate.exchange("http://localhost:"+ port +"/users/login",
                HttpMethod.POST,
                requestEntity,
                JwtResponseDTO.class
        );
    }

    @Then("the response with valid jwt token is sent to the user and the status code is {int}")
    public void the_system_should_respond_with_jwt_token(int expected) {
        assertEquals(HttpStatus.valueOf(expected), responseEntity.getStatusCode());
        JwtResponseDTO responseDTO = responseEntity.getBody();
        assertNotNull(responseDTO);
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("July");
        user.setLastName("Summer");
        trainee.setUser(user);
        Date dateOfBirth = new Date(91, Calendar.JUNE, 21);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Flower Street,90");
        registrationResponse = traineeService.save(trainee);
    }

}
