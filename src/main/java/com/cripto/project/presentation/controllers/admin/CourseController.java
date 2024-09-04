package com.cripto.project.presentation.controllers.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.CourseDtoRequest;
import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;
import com.cripto.project.domain.services.ICourseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Admin-Courses")
@RestController
@RequestMapping("/admin/courses")
public class CourseController {

    private final ICourseService courseService;

    CourseController(ICourseService service) {
        this.courseService = service;
    }

    @PostMapping(value = "/register-course", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, CourseDtoResponse>> registerCourse(
        @RequestBody @Valid CourseDtoRequest dto
    ) {
        return new ResponseEntity<>(this.courseService.register(dto), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<CourseDtoResponse>>> getCourses() {
        return new ResponseEntity<>(this.courseService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{courseId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, CourseDtoResponse>> getCourse(
        @PathVariable @Positive(message = "{Invalid.course.id}") Long courseId
    ) {
        return new ResponseEntity<>(this.courseService.getById(courseId), HttpStatus.OK);
    }

    @GetMapping(value = "/get-course-by-group",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<CourseDtoResponse>>> getCourseByNameAndGroup(
        @RequestParam @NotNull(message = "{NotNull.param.group.id}") @Positive(message = "{Invalid.param.group.id}") Long groupId,
        @RequestParam @NotNull(message = "{NotNull.param.course.name}") String courseName
    ) {
        return new ResponseEntity<>(this.courseService.getCourseByGroupAndNameContaining(groupId,courseName), HttpStatus.OK);
    }

    @PutMapping(value = "/update-course/{courseId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, CourseDtoResponse>> updateCourse(
        @PathVariable @Positive(message = "{Invalid.course.id}") Long courseId,
        @RequestBody @Valid CourseDtoRequest dto
    ) {
        return new ResponseEntity<>(this.courseService.update(courseId, dto), HttpStatus.OK);
    }

    @DeleteMapping(value= "/delete-course/{courseId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteCourse(
        @PathVariable @Positive(message = "{Invalid.course.id}") Long courseId
    ) {
        return new ResponseEntity<>(this.courseService.delete(courseId), HttpStatus.OK);
    }

    @PostMapping(value = "/add-student-to-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addStudentToCourse(
        @RequestParam @NotNull(message = "{NotNull.courseId}") @Positive(message = "{Invalid.courseId}") Long courseId,
        @RequestParam @NotNull(message = "{NotNull.courseId}") @Positive(message = "{Invalid.courseId}") Long studentId
    ) {
        return new ResponseEntity<>(this.courseService.addStudentToCourse(courseId, studentId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-student-to-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteStudentToCourse(
        @RequestParam @NotNull(message = "{NotNull.courseId}") @Positive(message = "{Invalid.courseId}") Long courseId,
        @RequestParam @NotNull(message = "{NotNull.courseId}") @Positive(message = "{Invalid.courseId}") Long studentId
    ) {
        return new ResponseEntity<>(this.courseService.deleteStudentToCourse(courseId, studentId), HttpStatus.OK);
    }
}
