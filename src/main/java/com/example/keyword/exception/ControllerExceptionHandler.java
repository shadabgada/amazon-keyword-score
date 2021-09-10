package com.example.keyword.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = JsonProcessingException.class)
    public String handleJsonProcessingException(JsonProcessingException jsonProcessingException) {
        log.warn("JsonProcessingException Found ", jsonProcessingException);
        return jsonProcessingException.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpClientErrorException.class)
    public String handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
        log.warn("HttpClientErrorException Found ", httpClientErrorException);
        return httpClientErrorException.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String handleCommonException(Exception exception) {
        log.warn("Exception Found ", exception);
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(value = HystrixRuntimeException.class)
    public String handleRequestTimeoutException(HystrixRuntimeException requestTimeoutException) {
        log.warn("TimeoutException Found: Request handling took more than 10 seconds ", requestTimeoutException);
        return requestTimeoutException.getMessage();
    }

}
