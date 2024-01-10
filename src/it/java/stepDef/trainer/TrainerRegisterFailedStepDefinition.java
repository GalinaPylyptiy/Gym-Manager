package stepDef.trainer;

import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainer.TrainerRegisterRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainerRegisterFailedStepDefinition {

    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    private String port;
    private HttpClientErrorException.BadRequest badRequestException;
    private HttpEntity<TrainerRegisterRequest> requestEntity;
    private TrainerRegisterRequest trainerRegisterRequest;

    @Given("a user sends the post request to the specified url for trainer register with specialization id {int}")
    public void a_user_send_the_post_request_to_the_url_with_invalid_specializationId(int id) {
        trainerRegisterRequest = new TrainerRegisterRequest();
        trainerRegisterRequest.setFirstName("Bill");
        trainerRegisterRequest.setLastName("Hugo");
        trainerRegisterRequest.setSpecializationId((long) id);
        requestEntity = new HttpEntity<>(trainerRegisterRequest);
    }

    @When("the system validates the requests and failed to save the trainer due to specialization id over max value")
    public void the_system_fails_to_save_the_trainee() {
        try {
            restTemplate.exchange("http://localhost:" + port + "/trainers",
                    HttpMethod.POST,
                    requestEntity,
                    RegistrationResponse.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with the status code {int} the bad request wrong specialization id")
    public void the_system_should_respond_with_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

}
