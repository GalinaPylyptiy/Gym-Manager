package com.epam.gym.client;

import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.exception.ExternalServerCallException;
import com.epam.gym.securityService.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrainingReportClient {

    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private static final String BASE_URL = "http://training-report";
    private static final String POST_URI = "/api/trainings";
    private static final Logger LOG = LoggerFactory.getLogger(TrainingReportClient.class);


    public TrainingReportClient(RestTemplate restTemplate, JwtService jwtService) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
    }

    public void postTraining(TrainingReportRequest request) {
        try{
            HttpEntity<TrainingReportRequest> requestEntity = new HttpEntity<>(request, getHeaders(request.getUsername()));
            restTemplate.exchange(
                    BASE_URL + POST_URI,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            LOG.info("Sending post request to url "+ BASE_URL + POST_URI );
        }catch (Exception ex){
            LOG.error("Error send post request to "+ BASE_URL);
            throw new ExternalServerCallException("Error sending request to the external server "+BASE_URL + POST_URI);
        }
    }

    private HttpHeaders getHeaders(String username){
        String transactionIdKey = "transactionId";
        String auth = "Authorization";
        String transactionId = MDC.get(transactionIdKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(transactionIdKey, transactionId);
        headers.set(auth, "Bearer "+ jwtService.generateToken(username));
        return headers;
    }

//TODO Вынести логику работу с JWT  и проверку в отдельный микросервис
//   TODO * Создать Spring boot starter!
}
