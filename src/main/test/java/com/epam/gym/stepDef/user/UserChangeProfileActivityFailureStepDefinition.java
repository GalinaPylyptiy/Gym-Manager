package com.epam.gym.stepDef.user;

import com.epam.gym.dto.ChangeActivityRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserChangeProfileActivityFailureStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private UserService userService;
    @LocalServerPort
    private String port;
    private RegistrationResponse registrationResponse;
    private HttpEntity<ChangeActivityRequest> requestEntity;
    private HttpClientErrorException.BadRequest badRequestException;


    @Given("a user sends a patch request to the specific url with ChangeActivityRequest body but pass empty activity status")
    public void a_user_send_the_patch_request_to_the_url_for_changing_profile_activity_with_invalid_data() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        ChangeActivityRequest request = new ChangeActivityRequest();
        request.setUsername(login);
        request.setPassword(password);
        requestEntity = new HttpEntity<>(request, getHeaders(jwt));

    }

    @When("a system receives the request and throws an exception after verifying that data is invalid")
    public void the_system_receives_request_and_throws_exception() {
        try {
            restTemplate.exchange("http://localhost:" + port + "/users/changeProfileActivity",
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the Exception is thrown with status {int} - Bad Request indicating that the profile activity was not changed")
    public void the_system_should_respond_with_status_400_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private void addTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Brown");
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
