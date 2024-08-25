package com.cripto.project.domain.dtos.consumes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "UserRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {
    private Long id;
    
    @NotNull(message = "{NotNull.user.name}")
    @NotBlank(message = "{Blank.user.name}")
    @Size(min = 2, max = 50, message = "{Length.user.name}")
    @Pattern(regexp = "^[A-Z].*$", message = "{InitCapitalLetter.user.name}")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "{OnlyLetters.user.name}")
    @Pattern(regexp = "^\\S+$", message = "{NotSpacesBlank.user.name}")
    private String name;

    @NotNull(message = "{NotNull.user.lastname}")
    @NotBlank(message = "{Blank.user.lastname}")
    @Size(min = 2, max = 50, message = "{Length.user.lastname}")
    @Pattern(regexp = "^[A-Z].*$", message = "{InitCapitalLetter.user.lastname}")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "{OnlyLetters.user.lastname}")
    @Pattern(regexp = "^\\S+$", message = "{NotSpacesBlank.user.lastname}")
    private String lastname;

    @NotNull(message = "{NotNull.user.email}")
    @NotBlank(message = "{Blank.user.email}")
    @Email(message = "{InvalidEmail.user.email}")
    private String email;
}