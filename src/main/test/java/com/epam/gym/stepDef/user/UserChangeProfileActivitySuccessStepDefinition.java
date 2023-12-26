package com.epam.gym.stepDef.user;

import com.epam.gym.dto.ChangeActivityRequest;
import com.epam.gym.dto.ChangePasswordRequest;
import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserChangeProfileActivitySuccessStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private UserService userService;
    @LocalServerPort
    private String port;
    private ResponseEntity <String> responseEntity;
    private RegistrationResponse registrationResponse;
    private HttpEntity<ChangeActivityRequest> requestEntity;


    @Given("a user sends a patch request to the specific url with ChangeActivityRequest body using valid data")
    public void a_user_send_the_patch_request_to_the_url_for_changing_profile_activity() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        ChangeActivityRequest request = new ChangeActivityRequest();
        request.setUsername(login);
        request.setPassword(password);
        request.setActive(false);
        requestEntity = new HttpEntity<>(request, getHeaders(jwt));

    }

    @When("a system receives the patch request and changes the profile activity for the specified user")
    public void the_system_receives_request_and_changes_activity() {
        responseEntity = restTemplate.exchange("http://localhost:"+ port +"/users/changeProfileActivity",
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );
    }

    @Then("the response with the status code {int} is returned indicating the success in changing profile activity")
    public void the_system_should_respond_with_status_200(int expected) {
        assertEquals(HttpStatus.valueOf(expected), responseEntity.getStatusCode());
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Will");
        user.setLastName("Smith");
        trainee.setUser(user);
        Date dateOfBirth = new Date(89, Calendar.DECEMBER, 12);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Baker Street,71");
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
