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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserLoginFailedStepDefinition {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TraineeService traineeService;
    @LocalServerPort
    private String port;
    private AuthenticationDTO authenticationDTO;
    private RegistrationResponse registrationResponse;
    private HttpEntity<AuthenticationDTO> requestEntity;
    private HttpClientErrorException.BadRequest badRequestException;


    @Given("a user sends a post request to the specific url for logging in but using invalid login")
    public void a_user_send_the_post_request_for_login_with_invalid_password() {
        addTrainee();
        authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername("invalid_username");
        authenticationDTO.setPassword(registrationResponse.getPassword());
        requestEntity = new HttpEntity<>(authenticationDTO);

    }

    @When("a system receives the request and throws an exception for invalid credentials")
    public void the_system_receives_request_and_verify_data_and_throw_auth_exception() {
        try {
            restTemplate.exchange("http://localhost:" + port + "/users/login",
                    HttpMethod.POST,
                    requestEntity,
                    JwtResponseDTO.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the HttpClientErrorException exception is thrown with status {int} - Bad Request")
    public void the_exception_is_thrown(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
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
