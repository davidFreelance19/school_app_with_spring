package com.cripto.project.domain.dtos.produces.course;

import java.util.List;
import java.util.ArrayList;

import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.entities.CourseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "CourseWithStudentsResponse")
@Getter
@Setter
@NoArgsConstructor
public class CourseWithStudentsDtoResponse extends CourseDtoResponse {

    private List<UserDtoResponse> students;

    public CourseWithStudentsDtoResponse(
        Long id, 
        String name, 
        GroupDtoResponse group, 
        UserDtoResponse teacher,
        List<UserDtoResponse> students
    ) {
        super(id, name, group, teacher);
        this.students = students;
    }

    public static CourseWithStudentsDtoResponse responseDto(CourseEntity entity) {
        CourseDtoResponse response = CourseDtoResponse.responseDto(entity);
        List<UserDtoResponse> students = new ArrayList<>();

        entity.getStudents().forEach(student-> {
            UserDtoResponse studentResponse = new UserDtoResponse(student.getId(), student.getName(), student.getLastname(), student.getEmail());
            students.add(studentResponse);
        });
        return new CourseWithStudentsDtoResponse(entity.getId(), entity.getName(), response.getGroup(), response.getTeacher(), students);
    }
}
