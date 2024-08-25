package com.cripto.project.domain.dtos.produces.qualification;

import com.cripto.project.domain.entities.QualificationEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "QualificationResponse")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualificationDtoResponse {

    private Long id;
    private Integer qualification;
    
    public static QualificationDtoResponse responseDto(QualificationEntity entity) {
        return new QualificationDtoResponse(entity.getId(), entity.getQualification());

    }
}
