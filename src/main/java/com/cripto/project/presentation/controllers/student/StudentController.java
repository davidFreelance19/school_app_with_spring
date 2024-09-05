package com.cripto.project.presentation.controllers.student;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.PasswordDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.services.IQualificattionService;
import com.cripto.project.domain.services.IUserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Students")
@RestController
@RequestMapping("/student")
public class StudentController {

    private final IQualificattionService qualificationService;
    private final IUserService userService;

    StudentController(IQualificattionService service, IUserService userService) {
        this.qualificationService = service;
        this.userService = userService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<QualificationsByStudentDtoResponse>>> getQualifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(this.qualificationService.getQualificationsByStudent(authentication.getPrincipal().toString()), HttpStatus.OK);
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> resetPassword(
        @RequestBody @Valid PasswordDtoRequest passwordDto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(this.userService.resetPassword(authentication.getPrincipal().toString(), passwordDto.getPassword()), HttpStatus.OK);
    }

}
