package com.cripto.project.presentation.controllers.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.EmailDtoRequest;
import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;
import com.cripto.project.domain.dtos.consumes.PasswordDtoRequest;
import com.cripto.project.domain.services.IAuthService;
import com.cripto.project.presentation.config.security.aspects.auth.RequireVerifyUserByToken;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private IAuthService authService;

    AuthController(IAuthService authService){
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(
        @RequestBody @Valid LoginDtoRequest loginRequest
    ){
        return new ResponseEntity<>(this.authService.login(loginRequest), HttpStatus.OK);    
    }

    @RequireVerifyUserByToken
    @GetMapping(value = "/verify-account/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> verifyUser(
        @PathVariable String token
    ){
        return new ResponseEntity<>(this.authService.verifyUser(token), HttpStatus.OK);    
    }

    @PostMapping(value = "/recupere-account", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> recupereAccount(
        @RequestBody @Valid EmailDtoRequest emailDtoRequest
    ){
        return new ResponseEntity<>(this.authService.recupereAccount(emailDtoRequest.getEmail()), HttpStatus.OK);    
    }

    @RequireVerifyUserByToken
    @PostMapping(value = "/reset-password/{token}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> resetPassword(
        @PathVariable String token,
        @RequestBody @Valid PasswordDtoRequest passwordDto
    ){
        return new ResponseEntity<>(this.authService.resetPassword(token, passwordDto.getPassword()), HttpStatus.OK);    
    }
}
