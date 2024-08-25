package com.cripto.project.domain.dtos.consumes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "CourseRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDtoRequest {
    private Long id;
    
    @NotNull(message = "{NotNull.name.course}")
    @NotBlank(message = "{NotBlank.name.course}")
    @Size(min = 3, max = 50, message = "{Length.name.course}")
    @Pattern(regexp = "^[A-Z]+(?> [A-Z]+)*$", message = "{Pattern.name.course}")
    private String name;

    @Positive(message = "{Invalid.teacherId}")
    private Long teacherId;
    
    @NotNull(message = "{NotNull.groupId}")
    @Positive(message = "{Invalid.groupId}")
    private Long groupId;
}
