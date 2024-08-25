package com.cripto.project.domain.dao;

import java.util.List;

import com.cripto.project.domain.entities.CourseEntity;
import com.cripto.project.domain.entities.UserEntity;

public interface ICourseDao extends ICrudDao<CourseEntity> {

    public void addStudentToCourse(Long id, UserEntity student);

    public void deleteStudentToCourse(Long id,  UserEntity student);

    public List<CourseEntity> getCourseByGroupAndNameContaining(Long groupId, String courseName);
}
