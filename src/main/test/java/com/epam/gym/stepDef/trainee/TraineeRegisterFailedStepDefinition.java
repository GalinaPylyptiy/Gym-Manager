package com.epam.gym.stepDef.trainee;

import com.epam.gym.dto.trainee.TraineeRegisterRequest;
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

public class TraineeRegisterFailedStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    private String port;
    private HttpClientErrorException.BadRequest badRequestException;
    private HttpEntity<TraineeRegisterRequest> requestEntity;

    @Given("a user sends the post request to the specified url with empty first name data")
    public void a_user_send_the_post_request_to_the_url_with_invalid_data() {
        TraineeRegisterRequest traineeRegisterRequest = new TraineeRegisterRequest();
        traineeRegisterRequest.setFirstName("");
        traineeRegisterRequest.setLastName("Soprano");
        Date dateOfBirth = new Date(70, Calendar.JUNE, 16);
        traineeRegisterRequest.setDateOfBirth(dateOfBirth);
        traineeRegisterRequest.setAddress("Walter Street,81");
        requestEntity = new HttpEntity<>(traineeRegisterRequest);
    }

    @When("the system validates the requests and failed to save the trainee")
    public void the_system_fails_to_save_the_trainee() {
        try {
            restTemplate.exchange("http://localhost:" + port + "/trainees",
                    HttpMethod.POST,
                    requestEntity,
                    HttpClientErrorException.BadRequest.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with the bad request the status code {int}")
    public void the_system_should_respond_with_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

}
