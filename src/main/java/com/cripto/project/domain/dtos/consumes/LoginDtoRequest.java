package com.cripto.project.domain.dtos.consumes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoRequest {
    
    @NotNull(message = "The username and password are required")
    @NotBlank(message = "The username and password cannot be blank")
    private String username;
    
    @NotNull(message = "The username and password are required")
    @NotBlank(message = "The username and password cannot be blank")
    private String password;

}
