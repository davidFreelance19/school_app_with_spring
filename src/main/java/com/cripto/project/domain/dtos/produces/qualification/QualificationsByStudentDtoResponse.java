package com.cripto.project.domain.dtos.produces.qualification;

import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;
import com.cripto.project.domain.entities.QualificationEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "QualificationsByStudentResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QualificationsByStudentDtoResponse extends QualificationDtoResponse{
    
    private CourseDtoResponse course;

    public QualificationsByStudentDtoResponse(Long id, Integer qualification, CourseDtoResponse course) {
        super(id, qualification);
        this.course = course;
    }

    public static QualificationsByStudentDtoResponse responseDto(QualificationEntity entity){
        CourseDtoResponse courseDto = CourseDtoResponse.responseDto(entity.getCourse());
        return new QualificationsByStudentDtoResponse(entity.getId(), entity.getQualification(), courseDto);
    }
    
}
