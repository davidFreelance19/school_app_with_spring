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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.services.IUserService;
import com.cripto.project.utils.RoleEnum;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@Tag(name = "Admin-Users")
@RequestMapping("/admin/users")
public class UserController {

    private final IUserService userService;

    UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register-student", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, UserDtoResponse>> registerStudent(
        @RequestBody @Valid UserDtoRequest user
    ) {
        return new ResponseEntity<>(this.userService.register(user, RoleEnum.STUDENT), HttpStatus.CREATED);
    }

    @PostMapping(value = "/register-teacher", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, UserDtoResponse>> registerTeacher(
        @RequestBody @Valid UserDtoRequest user
    ) {
        return new ResponseEntity<>(this.userService.register(user, RoleEnum.TEACHER), HttpStatus.CREATED);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<UserDtoResponse>>> getUsers() {
        return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, UserDtoResponse>> getUserById(
        @PathVariable @Positive(message = "{Invalid.id.user}") Long userId
    ) {
        return new ResponseEntity<>(this.userService.getById(userId), HttpStatus.OK);
    }

    @PutMapping(value = "/update-user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, UserDtoResponse>> updateUser(
        @PathVariable @Positive(message = "{Invalid.id.user}") Long userId, 
        @RequestBody @Valid UserDtoRequest user
    ) {
        return new ResponseEntity<>(this.userService.update(userId, user), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-student/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteStudent(
        @PathVariable @Positive(message = "{Invalid.id.user}") Long userId
    ) {
        return new ResponseEntity<>(this.userService.delete(userId), HttpStatus.OK);
    }

    
    @DeleteMapping(value = "/delete-teacher/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteTeacher(
        @PathVariable @Positive(message = "{Invalid.id.user}") Long userId
    ) {
        return new ResponseEntity<>(this.userService.delete(userId), HttpStatus.OK);
    }
}
