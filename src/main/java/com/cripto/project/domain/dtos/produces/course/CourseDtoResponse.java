package com.cripto.project.domain.dtos.produces.course;

import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.entities.CourseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "CourseResponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDtoResponse {

    private Long id;
    private String name;
    private GroupDtoResponse group;
    private UserDtoResponse teacher;

    public CourseDtoResponse(Long id, String name, GroupDtoResponse group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public static CourseDtoResponse responseDto(CourseEntity entity) {
        GroupDtoResponse groupDto = new GroupDtoResponse(entity.getGroup().getId(), entity.getGroup().getName());

        if (entity.getTeacher() != null){
            UserDtoResponse teacher = UserDtoResponse.responseDto(entity.getTeacher());
            return new CourseDtoResponse(entity.getId(), entity.getName(), groupDto, teacher);
        }

        return new CourseDtoResponse(entity.getId(), entity.getName(), groupDto);
    }

}
