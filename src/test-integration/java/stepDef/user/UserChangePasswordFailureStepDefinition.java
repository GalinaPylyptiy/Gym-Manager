package stepDef.user;

import com.epam.gym.dto.ChangePasswordRequest;
import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
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

public class UserChangePasswordFailureStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private UserService userService;
    @LocalServerPort
    private String port;
    private RegistrationResponse registrationResponse;
    private HttpEntity<ChangePasswordRequest> requestEntity;
    private HttpClientErrorException.BadRequest badRequestException;


    @Given("a user sends a patch request to the specific url with ChangePasswordRequest body but pass empty new password data")
    public void a_user_send_the_patch_request_to_the_url_for_changing_password_with_invalid_data() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername(login);
        changePasswordRequest.setOldPassword(password);
        changePasswordRequest.setNewPassword("");
        requestEntity = new HttpEntity<>(changePasswordRequest, getHeaders(jwt));
    }

    @When("a system receives the request and throws an exception for bad request due to validation error")
    public void the_system_receives_request_and_checks_data() {
        try {
            restTemplate.exchange("http://localhost:" + port + "/users/changePassword",
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the Exception is thrown with status {int} - Bad Request indicating that the password was not changed")
    public void the_system_should_respond_with_status_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Fin");
        user.setLastName("Fisher");
        trainee.setUser(user);
        Date dateOfBirth = new Date(99, Calendar.DECEMBER, 15);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Baker Street,18");
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
