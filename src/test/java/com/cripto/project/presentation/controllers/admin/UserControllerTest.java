package com.cripto.project.presentation.controllers.admin;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import static com.cripto.project.utils.GetAndValidateResponse.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private final String route = "/admin/users/";

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    void test_register_student_failded_by_validation_should_bad_request() throws JsonProcessingException {
        // given
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Invalid name") // Invalid name
                .email("test@example.com")
                .lastname("Invalid lastname!") // invalid lastname
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-student",
                HttpMethod.POST,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_register_student_success() throws JsonProcessingException {
        // given
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@student.com")
                .lastname("Student")
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-student",
                HttpMethod.POST,
                request,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // then
        validateResponse(user, response.getStatusCode(), HttpStatus.CREATED);

        testRestTemplate.exchange(
                route + "delete-student/" + user.get("user").getId(),
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    @Test
    void test_register_student_already_exist_should_return_conflict() throws JsonProcessingException {
        // given
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Conflict")
                .email("update@success.com")
                .lastname("Update")
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-student",
                HttpMethod.POST,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    void test_register_teacher_success() throws JsonProcessingException {
        // given
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@teacher.com")
                .lastname("Teacher")
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-teacher",
                HttpMethod.POST,
                request,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // then
        validateResponse(user, response.getStatusCode(), HttpStatus.CREATED);

        testRestTemplate.exchange(
                route + "delete-teacher/" + user.get("user").getId(),
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    @Test
    void test_get_user_by_id_success() throws JsonProcessingException {
        // given
        Long userId = 5L;
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + userId,
                HttpMethod.GET,
                request,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // then
        validateResponse(user, response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void test_user_not_found_by_id_should_return_not_found_exception() throws JsonProcessingException {
        // given
        Long userId = 500L;
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + userId,
                HttpMethod.GET,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void test_get_user_by_invalid_param_id_should_return_bad_request() throws JsonProcessingException {
        // given
        String userId = "abc";
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + userId,
                HttpMethod.GET,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_get_all_users_success() throws JsonProcessingException {
        // given
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route,
                HttpMethod.GET,
                request,
                String.class);
        Map<String, List<UserDtoResponse>> users = responseJson(response,
                new TypeReference<Map<String, List<UserDtoResponse>>>() {
                });

        // then
        validateResponse(users, response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void test_update_user_not_found_should_return_not_found_exception() throws JsonProcessingException {
        // given
        Long userId = 500L;
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@updated.com")
                .lastname("Conflict")
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "update-user/" + userId,
                HttpMethod.PUT,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void test_update_user_with_email_already_exist_should_return_conflict() throws JsonProcessingException {
        // given
        Long userId = 5L;
        UserDtoRequest requestRegisterDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@updated.com")
                .lastname("Conflict")
                .build();
        HttpEntity<UserDtoRequest> requestRegister = new HttpEntity<>(requestRegisterDto);

        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-student",
                HttpMethod.POST,
                requestRegister,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // when
        ResponseEntity<String> responseUpdated = testRestTemplate.exchange(
                route + "update-user/" + userId,
                HttpMethod.PUT,
                requestRegister,
                String.class);
        Map<String, UserDtoResponse> error = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // then
        validateResponse(error, responseUpdated.getStatusCode(), HttpStatus.CONFLICT);

        HttpEntity<Void> request = new HttpEntity<>(null);
        testRestTemplate.exchange(
                route + "delete-student/" + user.get("user").getId(),
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    @Test
    void test_update_user_success() throws JsonProcessingException {
        // given
        UserDtoRequest requestRegisterDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@updated.com")
                .lastname("Conflict")
                .build();
        HttpEntity<UserDtoRequest> requestRegister = new HttpEntity<>(requestRegisterDto);

        ResponseEntity<String> responseRegister = testRestTemplate.exchange(
                route + "register-student",
                HttpMethod.POST,
                requestRegister,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(responseRegister,
                new TypeReference<Map<String, UserDtoResponse>>() {
                });

        // when
        ResponseEntity<String> responseUpdated = testRestTemplate.exchange(
                route + "update-user/" + user.get("user").getId(),
                HttpMethod.PUT,
                requestRegister,
                String.class);
        Map<String, UserDtoResponse> userUpdated = responseJson(responseUpdated,
                new TypeReference<Map<String, UserDtoResponse>>() {
                });

        // then
        validateResponse(userUpdated, responseUpdated.getStatusCode(), HttpStatus.OK);

        HttpEntity<Void> request = new HttpEntity<>(null);
        testRestTemplate.exchange(
                route + "delete-student/" + user.get("user").getId(),
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    @Test
    void test_delete_student_not_found() throws JsonProcessingException {
        // given
        Long userId = 500L;
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "delete-student/" + userId,
                HttpMethod.DELETE,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void test_delete_teacher_success() throws JsonProcessingException {
        // given
        UserDtoRequest requestDto = UserDtoRequest.builder()
                .name("Test")
                .email("test@teacher.com")
                .lastname("Teacher")
                .build();
        HttpEntity<UserDtoRequest> request = new HttpEntity<>(requestDto);
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-teacher",
                HttpMethod.POST,
                request,
                String.class);
        Map<String, UserDtoResponse> user = responseJson(response, new TypeReference<Map<String, UserDtoResponse>>() {
        });

        // when
        ResponseEntity<String> responseToDelete = testRestTemplate.exchange(
                route + "delete-teacher/" + user.get("user").getId(),
                HttpMethod.DELETE,
                request,
                String.class);
        Map<String, String> message = responseJson(responseToDelete, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(message, responseToDelete.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void test_delete_user_with_dependencies_should_return_conflict() throws JsonProcessingException {
        // given
        Long userId = 5L;
        HttpEntity<Void> request = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "delete-student/" + userId,
                HttpMethod.DELETE,
                request,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.CONFLICT);
    }
}
