package com.cripto.project.presentation.controllers.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import com.cripto.project.domain.dtos.consumes.CourseDtoRequest;
import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;
import com.cripto.project.domain.dtos.produces.course.CourseWithStudentsDtoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import static com.cripto.project.utils.GetAndValidateResponse.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseControllerTest {

    public final String route = "/admin/courses/";

    @Autowired
    private TestRestTemplate testRestTemplate;

     /**
     * Tests the registration of a course when the specified group is not found.
     * 
     * @throws JsonProcessingException
     * 
     * @throws NotFoundException       when attempting to register a course with a
     *                                 non-existent group.
     */
    @Test
    void test_register_course_failed_by_not_found_group() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST")
                .groupId(300L)
                .build();
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

     /**
     * Tests the registration of a course when the specified teacher is not found.
     * 
     * @throws JsonProcessingException
     * 
     * @throws NotFoundException       when attempting to register a course with a
     *                                 non-existent teacher.
     */
    @Test
    void test_register_course_failed_by_not_found_teacher() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST")
                .groupId(1L)
                .teacherId(500L)
                .build();
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

     /**
     * Tests the registration of a course with invalid input data.
     * 
     * @throws JsonProcessingException
     * 
     * @throws BadRequestException     when the course data fails validation.
     */
    @Test
    void test_register_course_failed_validation() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("Test") // invalid pattern name
                .groupId(1L)
                .build();
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

     /**
     * Tests the registration of a course that already exists.
     * 
     * @throws JsonProcessingException
     * 
     * @throws ConflictException       when attempting to register a course with a
     *                                 name
     *                                 that already exists in the group.
     */
    @Test
    void test_register_course_already_exists_should_return_conflict() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST REQUEST")
                .groupId(1L)
                .build();

        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);
        ResponseEntity<String> responseRegisterCourse = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
                
        Map<String, CourseDtoResponse> course = responseJson(responseRegisterCourse, new TypeReference<Map<String, CourseDtoResponse>>() {
                });

        // when
        ResponseEntity<String> responseRegisterAlreadyExistCourse = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
        Map<String, String> error = responseJson(responseRegisterAlreadyExistCourse, new TypeReference<Map<String, String>>() {
                });
        
        // then
        validateResponse(error, responseRegisterAlreadyExistCourse.getStatusCode(), HttpStatus.CONFLICT);

        testRestTemplate.exchange(
                route + "delete-course/" + course.get("course").getId(),
                HttpMethod.DELETE,
                entity,
                String.class
        );
    }

     /**
     * Tests the successful registration of a new course.
     * @throws JsonProcessingException 
     */
    @Test
    void test_register_course_success() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST")
                .groupId(1L)
                .build();

        // when
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);
        ResponseEntity<String> responseRegisterCourse = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
                
        Map<String, CourseDtoResponse> course = responseJson(responseRegisterCourse, new TypeReference<Map<String, CourseDtoResponse>>() {
                });
        
        // then
        validateResponse(course, responseRegisterCourse.getStatusCode(), HttpStatus.CREATED);

        testRestTemplate.exchange(
                 route + "delete-course/" + course.get("course").getId(),
                 HttpMethod.DELETE,
                 entity,
                 String.class
         );
    }

     /**
     * Tests retrieving a course by its ID successfully.
     * @throws JsonProcessingException 
     */
    @Test
    void test_get_course_by_id_success() throws JsonProcessingException {
        // given
        Long courseId = 1L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + courseId,
                HttpMethod.GET,
                entity,
                String.class
        );
        Map<String, CourseWithStudentsDtoResponse> course = responseJson(response, new TypeReference<Map<String, CourseWithStudentsDtoResponse>>() {
                });
        
        // then
        validateResponse(course, response.getStatusCode(), HttpStatus.OK);
    }

     /**
     * Tests retrieving a non-existent course by ID.
     * @throws JsonProcessingException 
     * 
     * @throws NotFoundException when the requested course is not found.
     */
    @Test
    void test_not_found_course_by_id_should_return_not_found_exception() throws JsonProcessingException {
         // given
         Long courseId = 100L;
         HttpEntity<Void> entity = new HttpEntity<>(null);
 
         //when
         ResponseEntity<String> response = testRestTemplate.exchange(
                 route + courseId,
                 HttpMethod.GET,
                 entity,
                 String.class
         );
         Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                 });
         
         // then
         validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

     /**
     * Tests retrieving a course with an invalid (non-positive) ID.
     * @throws JsonProcessingException 
     * 
     * @throws BadRequestException when the course ID is not a positive number.
     */
    @Test
    void test_get_course_with_param_not_positive_id_should_return_bad_request() throws JsonProcessingException {
        // given
        Long courseId = 0L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + courseId,
                HttpMethod.GET,
                entity,
                String.class
        );
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                });
        
        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

     /**
     * Tests retrieving a course with an invalid (non-numeric) ID.
     * @throws JsonProcessingException 
     * 
     * @throws BadRequestException when the course ID is not a valid number.
     */
    @Test
    void test_get_course_with_invalid_id_should_return_bad_request() throws JsonProcessingException {
         // given
         String courseId = "abc";
         HttpEntity<Void> entity = new HttpEntity<>(null);
 
         //when
         ResponseEntity<String> response = testRestTemplate.exchange(
                 route + courseId,
                 HttpMethod.GET,
                 entity,
                 String.class
         );
         Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                 });
         
         // then
         validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

     /**
     * Tests retrieving all courses successfully.
     */
    @Test
    void test_get_courses_success() {
        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/admin/courses",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<String>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests retrieving courses with a specific name pattern and group ID.
     * @throws JsonProcessingException 
     */
    @Test
    void test_get_courses_with_containing_name_and_specific_group_success() throws JsonProcessingException {
        // given
        Long groupId = 1L;
        String courseName = "TEST";
        String urlWithParams = route + "get-course-by-group?groupId=" + groupId + "&courseName=" + courseName;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.GET,
                entity,
                String.class
        );

        Map<String, List<CourseDtoResponse>> courses = responseJson(response, new TypeReference<Map<String, List<CourseDtoResponse>>>() {
                 });
         
         // then
         validateResponse(courses, response.getStatusCode(), HttpStatus.OK);
    }

     /**
     * Tests retrieving courses for a non-existent group.
     * @throws JsonProcessingException 
     * 
     * @throws NotFoundException when the specified group is not found.
     */
    @Test
    void test_get_courses_with_containing_name_failed_by_group_not_found() throws JsonProcessingException {
        // given
        Long groupId = 100L;
        String courseName = "TEST";
        String urlWithParams = route + "get-course-by-group?groupId=" + groupId + "&courseName=" + courseName;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.GET,
                entity,
                String.class
        );

        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                 });
         
         // then
         validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * Tests retrieving courses with an invalid group ID.
     * @throws JsonProcessingException 
     * 
     * @throws BadRequestException when the group ID is not valid.
     */
    @Test
    void test_get_courses_with_containing_name_failed_by_group_not_valid() throws JsonProcessingException {
        // given
        String groupId = "abc";
        String courseName = "TEST";
        String urlWithParams = route + "get-course-by-group?groupId=" + groupId + "&courseName=" + courseName;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.GET,
                entity,
                String.class
        );

        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                 });
         
         // then
         validateResponse(error, response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Tests updating a non-existent course.
     * @throws JsonProcessingException 
     * 
     * @throws NotFoundException when the course to be updated is not found.
     */
    @Test
    void test_update_course_not_found_should_return_not_found_exception() throws JsonProcessingException {
        // given
        Long courseId = 400L;
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder().name("TEST").groupId(1L).build();
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "update-course/" + courseId,
                HttpMethod.PUT,
                entity,
                String.class
        );

        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                 });
        
        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * Tests updating a course with a name that already exists in the same group.
     * @throws JsonProcessingException 
     * 
     * @throws ConflictException when attempting to update a course with a name that
     *                           already exists in the group.
     */
    @Test
    void test_update_course_in_specific_group_and_name_already_exist_should_return_conflict() throws JsonProcessingException {
        // given
        Long courseId = 1L;
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST REQUEST")
                .groupId(1L)
                .build();

        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        ResponseEntity<String> responseRegisterCourse = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class
        );
        
        Map<String, CourseDtoResponse> course = responseJson(responseRegisterCourse, new TypeReference<Map<String, CourseDtoResponse>>() {
        });

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "update-course/" + courseId,
                HttpMethod.PUT,
                entity,
                String.class
        );
        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.CONFLICT);

        testRestTemplate.exchange(
                route + "delete-course/" + course.get("course").getId(),
                HttpMethod.DELETE,
                entity,
                String.class
        );
    }

    /**
     * Tests successful update of a course.
     * @throws JsonProcessingException 
     */
    @Test
    void test_update_course_success() throws JsonProcessingException {
        // given
        Long courseId = 1L;
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder().name("TEST").groupId(1L).build();
        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);

        // then
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "update-course/" + courseId,
                HttpMethod.PUT,
                entity,
                String.class
        );
        Map<String, CourseDtoResponse> courseUpdated = responseJson(response, new TypeReference<Map<String, CourseDtoResponse>>() {
        });

        // then
        validateResponse(courseUpdated, response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Tests deleting a non-existent course.
     * @throws JsonProcessingException 
     * 
     * @throws NotFoundException when the course to be deleted is not found.
     */
    @Test
    void test_delete_course_failed_by_not_found_should_return_not_found_exception() throws JsonProcessingException {
        // given
        Long courseId = 100L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "delete-course/" + courseId,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(error, response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * Tests the deletion of a course with dependencies.
     * @throws JsonProcessingException 
     * 
     * @throws Conflict when attempting to delete a course
     *                                           that has dependencies (e.g.,
     *                                           enrolled students).
     */
    @Test
    void test_delete_course_with_dependencies_return_conflict() throws JsonProcessingException {
        // given
        Long courseId = 1L;
        HttpEntity<Void> entity = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "delete-course/" + courseId,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        Map<String, String> error = responseJson(response, new TypeReference<Map<String, String>>() {
                });
       
       // then
       validateResponse(error, response.getStatusCode(), HttpStatus.CONFLICT);
    }

    /**
     * Tests the successful deletion of a course.
     * @throws JsonProcessingException 
     */
    @Test
    void test_delete_course_success() throws JsonProcessingException {
        // given
        CourseDtoRequest courseDtoRequest = CourseDtoRequest.builder()
                .name("TEST TO DELETE")
                .groupId(1L)
                .build();

        HttpEntity<CourseDtoRequest> entity = new HttpEntity<>(courseDtoRequest);
        ResponseEntity<String> responseRegisterCourse = testRestTemplate.exchange(
                route + "register-course",
                HttpMethod.POST,
                entity,
                String.class);
                
        Map<String, CourseDtoResponse> course = responseJson(responseRegisterCourse, new TypeReference<Map<String, CourseDtoResponse>>() {
                });
        HttpEntity<Void> entityToDelete = new HttpEntity<>(null);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
                route + "delete-course/" + course.get("course").getId(),
                HttpMethod.DELETE,
                entityToDelete,
                String.class
        );

        Map<String, String> message = responseJson(response, new TypeReference<Map<String, String>>() {
        });

        // then
        validateResponse(message, response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Tests adding a non-existent student to a specific course.
     * 
     * @throws HttpClientErrorException.NotFound when attempting to add a student
     *                                           that doesn't exist in the system.
     */
    @Test
    void test_add_student_not_found_in_specific_course_should_return_not_found_exception() {
        Long studentId = 100L;
        Long courseId = 1L;

        String urlWithParams = route + "add-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests adding a student to a non-existent course.
     * 
     * @throws HttpClientErrorException.NotFound when attempting to add a student
     *                                           to a course that doesn't exist in
     *                                           the system.
     */
    @Test
    void test_add_student_in_specific_course_not_found_should_return_not_found_exception() {
        Long studentId = 5L;
        Long courseId = 100L;

        String urlWithParams = route + "add-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests adding a student to a course with invalid parameters.
     * 
     * @throws HttpClientErrorException.BadRequest when providing invalid
     *                                             parameters for adding a student
     *                                             to a course.
     */
    @Test
    void test_add_student_in_specific_course_invalid_params_return_bad_request() {
        String studentId = "ABC";
        Long courseId = 100L;

        String urlWithParams = route + "add-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Tests the successful addition of a student to a specific course.
     * 
     * @throws HttpClientErrorException if adding the student to the course fails
     *                                  for any reason.
     */
    @Test
    void test_add_student_in_specific_course_success() {
        Long studentId = 5L;
        Long courseId = 1L;

        String urlWithParams = route + "add-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests adding a student who is already registered in a specific course.
     * 
     * @throws HttpClientErrorException.Conflict when attempting to add a student
     *                                           who is already registered in the
     *                                           specified course.
     */
    @Test
    void test_add_student_already_register_in_specific_course_should_return_conflic() {
        Long studentId = 5L;
        Long courseId = 1L;

        String urlWithParams = route + "add-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Tests deleting a non-existent student from a specific course.
     * 
     * @throws HttpClientErrorException.NotFound when attempting to delete a student
     *                                           that doesn't exist in the specified
     *                                           course.
     */
    @Test
    void test_delete_student_not_found_in_specific_course_should_return_not_found_exception() {
        Long studentId = 500L;
        Long courseId = 5L;

        String urlWithParams = route + "delete-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests the successful deletion of a student from a specific course.
     * 
     * @throws HttpClientErrorException if deleting the student from the course
     *                                  fails
     *                                  for any reason.
     */
    @Test
    void test_delete_to_specific_course_student_success() {
        Long studentId = 5L;
        Long courseId = 1L;

        String urlWithParams = route + "delete-student-to-course?studentId=" + studentId + "&courseId=" + courseId;

        HttpEntity<Void> entity = new HttpEntity<>(null);
        ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
                urlWithParams,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
