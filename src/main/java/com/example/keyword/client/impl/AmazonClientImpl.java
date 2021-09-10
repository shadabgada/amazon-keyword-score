package com.example.keyword.client.impl;

import com.example.keyword.client.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class AmazonClientImpl implements AmazonClient {

    private static final RestTemplate restTemplate = new RestTemplateBuilder().build();

    /**
     * Returns a string response by making a resttemplate call to amazon api based on input keyword
     *
     * @param keyword
     * @return
     */
    @Override
    public String getSearchResponse(String keyword) {
        log.info("BEGIN: AmazonClientImpl::getSearchResponse ");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://completion.amazon.com/api/2017/suggestions")
                .encode()
                .queryParam("prefix", keyword.replace(" ", "+"))
                .queryParam("mid", "ATVPDKIKX0DER")
                .queryParam("alias", "aps")
                .queryParam("limit", 10)
                .queryParam("suggestion-type", "KEYWORD");

        String response = restTemplate.getForObject(builder.toUriString(), String.class);

        log.info("END: AmazonClientImpl::getSearchResponse ");
        return response;
    }
}
