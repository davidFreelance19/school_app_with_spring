package com.cripto.project.domain.dtos.produces.qualification;

import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.entities.QualificationEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "QualificationsByCourseResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QualificationsByCourseDtoResponse extends QualificationDtoResponse{
    private UserDtoResponse student;

    public QualificationsByCourseDtoResponse(Long id, Integer qualification, UserDtoResponse userDtoResponse) {
        super(id, qualification);
        this.student = userDtoResponse;
    }

    public static QualificationsByCourseDtoResponse responseDto(QualificationEntity entity){
        UserDtoResponse user = UserDtoResponse.responseDto(entity.getStudent());
        return new QualificationsByCourseDtoResponse(entity.getId(), entity.getQualification(), user);
    }
}
