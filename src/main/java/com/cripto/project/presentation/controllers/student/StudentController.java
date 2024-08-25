package com.cripto.project.presentation.controllers.student;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.services.QualificattionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Students")
@RestController
@RequestMapping("/student")
public class StudentController {
    
    private final QualificattionService qualificationService;

    StudentController(QualificattionService service){
        this.qualificationService = service;
    }
    
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<QualificationsByStudentDtoResponse>>> getQualifications(){
        return new ResponseEntity<>(this.qualificationService.getQualificationsByStudent(1L), HttpStatus.OK);
    }
}
