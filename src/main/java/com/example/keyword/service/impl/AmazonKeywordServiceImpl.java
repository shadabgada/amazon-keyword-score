package com.example.keyword.service.impl;

import com.example.keyword.client.AmazonClient;
import com.example.keyword.service.AmazonKeywordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AmazonKeywordServiceImpl implements AmazonKeywordService {

    @Autowired
    @Qualifier("amazonClientImpl")
    AmazonClient amazonClient;

    /**
     * Returns a score between 0-100 for a given keyword
     *
     * @param keyword
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Integer getScore(String keyword) throws JsonProcessingException {
        log.info("BEGIN: AmazonKeywordServiceImpl::getScore ");

        String response = amazonClient.getSearchResponse(keyword);
        ArrayNode jsonNodes = (ArrayNode) new ObjectMapper().readTree(response).get("suggestions");
        List<String> suggestedKeywords = new ArrayList<>();
        jsonNodes.elements().forEachRemaining(jsonNode -> suggestedKeywords.add(jsonNode.get("value").textValue()));

        double score = suggestedKeywords.stream().map(s -> {
            if (s.equals(keyword)) {
                return 3.3333;
            }
            return similarity(s, keyword);
        }).mapToDouble(Number::doubleValue).sum();

        score = score * 10;

        if (score > 100)
            score = 100;

        log.info("END: AmazonKeywordServiceImpl::getScore ");
        return (int) score;
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }


    /**
     * returns a number indicating difference between two strings based on Levenshtein Distance algorithm
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}
