package com.cripto.project.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResultMatcher {
 
        public static ResultMatcher containsError(String expectedMessage) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                String jsonResponse = result.getResponse().getContentAsString();
                ObjectMapper mapper = new ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(jsonResponse);

                String actualMessage = root.get("error").asText();
                assertEquals(expectedMessage, actualMessage);
            }
        };
    }
}
