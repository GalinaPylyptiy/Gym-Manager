package stepDef.trainer;

import com.epam.gym.dto.JwtResponseDTO;
import com.epam.gym.dto.RegistrationResponse;
import com.epam.gym.dto.trainee.TraineeProfileResponse;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.model.User;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainerGetProfileFailedStepDefinition {

    @LocalServerPort
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private HttpEntity<?> requestEntity;

    private RegistrationResponse registrationResponse;

    private HttpClientErrorException.BadRequest badRequestException;

    private String urlWithParams;

    @Given("a user sends the get request to the specified url for trainer getting profile, but input invalid username")
    public void a_user_sends_the_get_request_to_the_specified_url_with_invalid_username() {
        addTrainer();
        String login = registrationResponse.getLogin();
        String password = registrationResponse.getPassword();
        String jwt = getToken(login, password);
        requestEntity = new HttpEntity<>(getHeaders(jwt));
        String invalidLogin = "invalidLogin";
        String url = "http://localhost:" + port + "/trainers";
        urlWithParams = UriComponentsBuilder.fromUriString(url)
                .queryParam("username", invalidLogin)
                .queryParam("password", password)
                .toUriString();
    }

    @When("the system checks the credentials and failed to retrieve trainer info and throws an exception")
    public void the_system_get_the_request_and_throws_an_exception() {
        try {
            restTemplate.exchange(urlWithParams,
                    HttpMethod.GET,
                    requestEntity,
                    TraineeProfileResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            badRequestException = e;
        }
    }

    @Then("the system should respond with the status code {int} the bad request the username not found in the database")
    public void assert_status_400_bad_request(int expected) {
        assertNotNull(badRequestException);
        assertEquals(HttpStatus.valueOf(expected), badRequestException.getStatusCode());
    }

    private HttpHeaders getHeaders(String token) {
        String auth = "Authorization";
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, "Bearer " + token);
        return headers;
    }

    private void addTrainer(){
        Trainer trainer = new Trainer();
        TrainingType specialization = getTrainingType();
        User user = new User();
        user.setFirstName("Nick");
        user.setLastName("Petrov");
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        registrationResponse = trainerService.save(trainer);
    }

    private TrainingType getTrainingType(){
        return trainingTypeService.findById(4L);
    }

    private String getToken(String login, String password) {
        JwtResponseDTO responseDTO = userService.login(login, password);
        return responseDTO.getJwt();
    }

}
