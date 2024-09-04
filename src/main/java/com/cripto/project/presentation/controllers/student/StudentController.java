package com.cripto.project.presentation.controllers.student;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.services.IQualificattionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Students")
@RestController
@RequestMapping("/student")
public class StudentController {

    private final IQualificattionService qualificationService;

    StudentController(IQualificattionService service) {
        this.qualificationService = service;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<QualificationsByStudentDtoResponse>>> getQualifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(this.qualificationService.getQualificationsByStudent(authentication.getPrincipal().toString()), HttpStatus.OK);
    }
}
