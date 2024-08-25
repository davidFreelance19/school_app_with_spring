package com.cripto.project.presentation.controllers.teacher;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.services.QualificattionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Tag(name = "Teacher")
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final QualificattionService qualificationService;

    TeacherController(QualificattionService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @GetMapping(value = "/get-qualifications-by-course/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<QualificationsByCourseDtoResponse>>> getQualificationsByCourse(
        @PathVariable @Positive(message = "{Invalid.course.id}") Long courseId
    ) {
        return new ResponseEntity<>(this.qualificationService.getQualificationsByCourse(courseId), HttpStatus.OK);
    }

    @GetMapping(value = "/get-qualification/{qualificationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> getQualification(
        @PathVariable @Positive(message = "{Invalid.id.qualification}") Long qualificationId
    ) {
        return new ResponseEntity<>(this.qualificationService.getById(qualificationId), HttpStatus.OK);
    }

    @GetMapping(value = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<QualificationsByCourseDtoResponse>>> getCoursesToTeacher() {
        return new ResponseEntity<>(this.qualificationService.getQualificationsByCourse(1l), HttpStatus.OK);
    }

    @PostMapping(value = "/register-qualification-by-student", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> registerQualification(
        @RequestBody @Valid QualificationDtoRequest dto
    ) {
        return new ResponseEntity<>(this.qualificationService.register(dto), HttpStatus.OK);
    }

    @PutMapping(value = "/update-qualification/{qualificationId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, QualificationsByStudentDtoResponse>> updateQualification(
        @PathVariable @Positive(message = "{Invalid.id.qualification}") Long qualificationId,
        @RequestBody @Valid QualificationDtoRequest dto
    ) {
        return new ResponseEntity<>(this.qualificationService.update(qualificationId, dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-qualification/{qualificationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteQualification(
        @PathVariable @Positive(message = "{Invalid.id.qualification}") Long qualificationId
    ) {
        return new ResponseEntity<>(this.qualificationService.delete(qualificationId), HttpStatus.OK);
    }

}
