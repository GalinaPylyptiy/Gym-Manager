package com.epam.gym.stepDef.trainee;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TraineeGetProfileFailedStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    private HttpEntity<?> requestEntity;

    private RegistrationResponse registrationResponse;

    private HttpClientErrorException.BadRequest badRequestException;

    private String urlWithParams;

    @Given("a user logs in success and sends the get request to the specified url with bad credentials to fetch the trainee profile")
    public void a_user_sends_the_get_request_to_the_specified_url_with_incorrect_password() {
        addTrainee();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        requestEntity = new HttpEntity<>(getHeaders(jwt));
        String invalidLogin = "invalidLogin";
        String url = "http://localhost:" + port + "/trainees";
        urlWithParams = UriComponentsBuilder.fromUriString(url)
                .queryParam("username", invalidLogin)
                .queryParam("password", password)
                .toUriString();
    }

    @When("the system receives a request to get the trainee profile and return throws an exception")
    public void the_system_receives_the_request_and_throws_an_exception() {
        try {
            restTemplate.exchange(urlWithParams,
                    HttpMethod.GET,
                    requestEntity,
                    TraineeProfileResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with the status {int} - Bad Credentials")
    public void assert_failed_status(int expected) {
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
        user.setFirstName("Mike");
        user.setLastName("Petrov");
        trainee.setUser(user);
        Date dateOfBirth = new Date(71, Calendar.JUNE, 20);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Walter Street,90");
        registrationResponse = traineeService.save(trainee);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

}
