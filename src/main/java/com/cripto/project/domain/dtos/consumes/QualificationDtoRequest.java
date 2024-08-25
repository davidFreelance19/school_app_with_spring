package com.cripto.project.domain.dtos.consumes;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "QualificationRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDtoRequest {
    private Long id;

    @NotNull(message = "{NotNull.value.qualification}")
    @Min(value = 5, message = "{Min.value.qualification}")
    @Max(value = 10, message = "{Max.value.qualification}")
    private Integer qualification;

    @NotNull(message = "{NotNull.courseId}")
    @Positive(message = "{Invalid.courseId}")
    private Long courseId;

    @NotNull(message = "{NotNull.studentId}")
    @Positive(message = "{Invalid.studentId}")
    private Long studentId;
}
