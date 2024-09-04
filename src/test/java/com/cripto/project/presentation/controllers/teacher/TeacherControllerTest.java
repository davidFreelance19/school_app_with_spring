package com.cripto.project.presentation.controllers.teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherControllerTest {
    private final String route = "/teacher";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private <T> void validateResponse(ResponseEntity<Map<String, T>> response, HttpStatus expectedStatus) {
        assertNotNull(response.getBody());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void test_register_qualification_failed_by_validation(){
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(0L) // Not positive
            .qualification(10)
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/register-qualification-by-student",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_register_qualification_failed_by_course_not_found(){
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(300L)
            .studentId(5L)
            .qualification(10)
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/register-qualification-by-student",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_register_qualification_failed_by_student_not_found(){
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(500L)
            .qualification(10)
            .build();
            
        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/register-qualification-by-student",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_register_qualification_alredy_exist_should_return_conflict(){
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(5L) 
            .qualification(10)
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/register-qualification-by-student",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.CONFLICT);
    }

    @Test
    void test_register_qualification_success(){
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(5L)
            .qualification(10)
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> response = testRestTemplate.exchange(
                route + "/register-qualification-by-student",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, QualificationsByStudentDtoResponse>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }

    @Test
    void test_get_teacher_by_id_success() {
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, List<QualificationsByCourseDtoResponse>>> response = testRestTemplate.exchange(
                route + "/courses",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, List<QualificationsByCourseDtoResponse>>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }

    @Test
    void test_not_found_qualification_should_return_not_found_exception() {
        Long qualificationId = 300L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/get-qualification/" + qualificationId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_get_qualification_with_invalid_id_in_params_should_return_bad_request() {
        String qualificationId = "abc";
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/get-qualification/" + qualificationId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_get_qualification_with_not_positive_id_in_params_should_return_bad_request() {
        Long qualificationId = 0L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/get-qualification/" + qualificationId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_get_qualification_by_id_success() {
        Long qualificationId = 1L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> response = testRestTemplate.exchange(
                route + "/get-qualification/" + qualificationId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, QualificationsByStudentDtoResponse>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }

    @Test
    void test_get_qualifications_by_course_not_found_should_return_not_found_exception() {
        Long courseId = 300L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/get-qualifications-by-course/" + courseId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_get_qualifications_by_specific_course_success() {
        Long courseId = 1L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, List<QualificationsByCourseDtoResponse>>> response = testRestTemplate.exchange(
                route + "/get-qualifications-by-course/" + courseId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, List<QualificationsByCourseDtoResponse>>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }

    @Test
    void test_update_qualification_not_found_should_return_not_found_exception() {
        Long qualificationId = 1L;
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(1L)
            .qualification(5)
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/update-qualification/" + qualificationId,
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_update_qualification_failed_by_validation_should_return_bad_request() {
        Long qualificationId = 1L;
        
        QualificationDtoRequest request = QualificationDtoRequest.builder()
            .courseId(1L)
            .studentId(1L) 
            .qualification(0) // qualification invalid
            .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/update-qualification/" + qualificationId,
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void test_update_qualification_success() {
        Long qualificationId = 4L;
        QualificationDtoRequest request = QualificationDtoRequest.builder()
        .courseId(1L)
        .studentId(1L)
        .qualification(5)
        .build();

        HttpEntity<QualificationDtoRequest> entity = new HttpEntity<>(request);

        ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> response = testRestTemplate.exchange(
                route + "/update-qualification/" + qualificationId,
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<Map<String, QualificationsByStudentDtoResponse>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }
    
    @Test
    void test_delte_qualification_not_found_should_return_not_found_exception() {
        Long qualificationId = 300L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/delete-qualification/" + qualificationId,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void test_delete_qualification_sucess() {
        Long qualificationId = 2L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                route + "/delete-qualification/" + qualificationId,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response, HttpStatus.OK);
    }
}
