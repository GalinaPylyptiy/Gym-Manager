package com.epam.gym.handler;

import com.epam.gym.dto.exception.ApiExceptionDetails;
import com.epam.gym.exception.AuthenticationFailedException;
import com.epam.gym.exception.ExceedAttemptsLimitException;
import com.epam.gym.exception.ExternalServerCallException;
import com.epam.gym.exception.InvalidJwtTokenException;
import com.epam.gym.exception.JMSException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {InvalidJwtTokenException.class})
    public ResponseEntity<ApiExceptionDetails> handleInvalidTokenException(InvalidJwtTokenException ex) {
        return new ResponseEntity<>(getExDetails(ex,HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AuthenticationFailedException.class})
    public ResponseEntity<ApiExceptionDetails> handleAuthFailedEx(AuthenticationFailedException ex) {
        return new ResponseEntity<>(getExDetails(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RollbackException.class})
    public ResponseEntity<ApiExceptionDetails> handleRollbackException(RollbackException ex) {
        return new ResponseEntity<>(getExDetails(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ApiExceptionDetails> handleEntityNotFoundEx(EntityNotFoundException ex) {
        return new ResponseEntity<>(getExDetails(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ExceedAttemptsLimitException.class})
    public ResponseEntity<ApiExceptionDetails> handleLimitAttemptsEx(ExceedAttemptsLimitException ex) {
        return new ResponseEntity<>(getExDetails(ex,HttpStatus.FORBIDDEN ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {JMSException.class})
    public ResponseEntity<ApiExceptionDetails> handleJMSException(JMSException ex) {
        return new ResponseEntity<>(getExDetails(ex,HttpStatus.SERVICE_UNAVAILABLE), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = {ExternalServerCallException.class})
    public ResponseEntity<ApiExceptionDetails> handleExternalServerCallException(ExternalServerCallException ex) {
        return new ResponseEntity<>(getExDetails(ex,HttpStatus.SERVICE_UNAVAILABLE), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<ApiExceptionDetails>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiExceptionDetails> exceptionDetailsList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            var exceptionDetail = new ApiExceptionDetails(
                    error.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now()
            );
            exceptionDetailsList.add(exceptionDetail);
        });
        return new ResponseEntity<>(exceptionDetailsList, HttpStatus.BAD_REQUEST);
    }

    private ApiExceptionDetails getExDetails(Exception ex, HttpStatus status) {
        return new ApiExceptionDetails(
                ex.getMessage(),
                status,
                LocalDateTime.now());
    }
}
