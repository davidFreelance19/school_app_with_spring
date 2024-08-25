package com.cripto.project.domain.dtos.produces.group;

import java.util.ArrayList;
import java.util.List;

import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;
import com.cripto.project.domain.entities.GroupEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupWithCoursesDtoResponse extends GroupDtoResponse {

    private List<CourseDtoResponse> courses;

    public GroupWithCoursesDtoResponse(Long id, String name, List<CourseDtoResponse> courses) {
        super(id, name);
        this.courses = courses;
    }

    public static GroupWithCoursesDtoResponse response(GroupEntity entity) {
        List<CourseDtoResponse> courses = new ArrayList<>();
        entity.getCourses().forEach(course -> courses.add(CourseDtoResponse.responseDto(course)));
        return new GroupWithCoursesDtoResponse(entity.getId(), entity.getName(), courses);
    }
}
