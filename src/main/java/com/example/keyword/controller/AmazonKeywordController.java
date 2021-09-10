package com.example.keyword.controller;

import com.example.keyword.dto.keywordScore;
import com.example.keyword.service.AmazonKeywordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/estimate")
@EnableHystrix
public class AmazonKeywordController {

    @Autowired
    @Qualifier("amazonKeywordServiceImpl")
    AmazonKeywordService amazonKeywordService;

    /**
     * Returns the keyword and its score
     * this function is monitored by Hystrix circuit breaker,
     * if the execution time takes more than 10 second, An error will be returned
     *
     * @param keyword
     * @return
     * @throws JsonProcessingException
     */
    @HystrixCommand(fallbackMethod = "timedOut", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    @GetMapping
    public ResponseEntity<?> getScore(@RequestParam("keyword") String keyword) throws JsonProcessingException {

        return ResponseEntity.ok(new keywordScore(keyword, amazonKeywordService.getScore(keyword)));
    }

    /**
     * Fallback function called by hystrix when request takes more than 10 seconds
     *
     * @param keyword
     * @return
     */
    public ResponseEntity<?> timedOut(String keyword) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request handling took more than 10 seconds");
    }
}
