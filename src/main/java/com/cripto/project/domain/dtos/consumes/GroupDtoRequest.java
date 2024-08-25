package com.cripto.project.domain.dtos.consumes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "GroupRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDtoRequest {
    private Long id;
    
    @NotNull(message = "{NotNull.name.group}")
    @NotBlank(message = "{Blank.name.group}")
    @Size(min = 4, max = 5, message = "{Length.name.group}")
    @Pattern(regexp = "^\\d[A-Z]{2}\\d+$", message = "{Invalid.name.group}")
    private String name;

}
