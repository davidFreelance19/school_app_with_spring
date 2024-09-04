package com.cripto.project.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetAndValidateResponse {
  
    public static <T> void validateResponse(Map<String, T> response, HttpStatusCode actualStatus, HttpStatus expectedStatus) {
        assertNotNull(response);
        assertEquals(expectedStatus, actualStatus);
    }

    public static <T> Map<String, T> responseJson(
            ResponseEntity<String> response,
            TypeReference<Map<String, T>> typeRef
    ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(
                response.getBody(),
                typeRef);

    }
}
