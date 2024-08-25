package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

import com.cripto.project.domain.dtos.consumes.CourseDtoRequest;
import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;

public interface CourseService extends CrudService<CourseDtoResponse, CourseDtoRequest>{

    public Map<String, String> addStudentToCourse(Long id, Long studentId);

    public Map<String, String> deleteStudentToCourse(Long id, Long studentId);

    public Map<String, List<CourseDtoResponse>> getCourseByGroupAndNameContaining(Long groupId, String courseName);
}
