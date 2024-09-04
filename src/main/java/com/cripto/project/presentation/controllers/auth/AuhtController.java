package com.cripto.project.presentation.controllers.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;
import com.cripto.project.domain.services.IAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuhtController {
    
    private IAuthService authService;

    AuhtController(IAuthService authService){
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(
        @RequestBody @Valid LoginDtoRequest loginRequest
    ){
        return new ResponseEntity<>(this.authService.login(loginRequest), HttpStatus.OK);    
    }
}
