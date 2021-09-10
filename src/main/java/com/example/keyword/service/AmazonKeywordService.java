package com.example.keyword.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AmazonKeywordService {

    Integer getScore(String keyword) throws JsonProcessingException;

}
