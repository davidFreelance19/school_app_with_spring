package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.ICourseDao;
import com.cripto.project.domain.dtos.consumes.CourseDtoRequest;
import com.cripto.project.domain.dtos.produces.course.CourseDtoResponse;
import com.cripto.project.domain.dtos.produces.course.CourseWithStudentsDtoResponse;
import com.cripto.project.domain.entities.CourseEntity;
import com.cripto.project.domain.entities.UserEntity;
import com.cripto.project.domain.services.CourseService;
import com.cripto.project.domain.services.GroupService;
import com.cripto.project.domain.services.UserService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String COURSE = "course";
    private static final String COURSES = "courses";
    private static final String MESSAGE = "message";

    private final ICourseDao courseRepository;
    private final UserService userService;
    private final GroupService groupService;

    CourseServiceImpl(ICourseDao repository, GroupService groupService, UserService userService) {
        this.courseRepository = repository;
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public final Map<String, CourseDtoResponse> register(CourseDtoRequest dto) {
        try {
            validateGroupAndTeacher(dto);

            ModelMapper mapper = new ModelMapper();
            CourseEntity entity = mapper.map(dto, CourseEntity.class);

            CourseDtoResponse response = CourseDtoResponse.responseDto(this.courseRepository.register(entity));
            return Map.of(COURSE, response);
        } catch (EntityExistsException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, List<CourseDtoResponse>> getAll() {
        List<CourseDtoResponse> courses = this.courseRepository.getAll().stream()
                .map(CourseDtoResponse::responseDto).collect(Collectors.toList());

        return Map.of(COURSES, courses);
    }

    @Override
    public final Map<String, CourseDtoResponse> getById(Long id) {
        try {
            CourseDtoResponse response = CourseWithStudentsDtoResponse.responseDto(this.courseRepository.getById(id));
            return Map.of(COURSE, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, CourseDtoResponse> update(Long courseId, CourseDtoRequest dto) {
        try {
            validateGroupAndTeacher(dto);
            ModelMapper mapper = new ModelMapper();
            CourseEntity entity = mapper.map(dto, CourseEntity.class);

            CourseDtoResponse response = CourseDtoResponse.responseDto(this.courseRepository.update(courseId, entity));
            return Map.of(COURSE, response);
        } catch (EntityExistsException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, String> delete(Long courseId) {
        try {
            this.courseRepository.delete(courseId);
            return Map.of(MESSAGE, "Course deleted successfully");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION);
        }
    }

    @Override
    public final Map<String, String> addStudentToCourse(Long id, Long studentId) {
        try {
            UserEntity student = studentExist(studentId);
            this.courseRepository.addStudentToCourse(id, student);
            return Map.of(MESSAGE, "Student successfully registered in this course");
        } catch (EntityExistsException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, String> deleteStudentToCourse(Long id, Long studentId) {
        try {
            UserEntity student = studentExist(studentId);
            this.courseRepository.deleteStudentToCourse(id, student);
            return Map.of(MESSAGE, "Student successfully removed in this course");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, List<CourseDtoResponse>> getCourseByGroupAndNameContaining(
        Long groupId,
        String courseName
    ) {
        try {
            this.groupService.getById(groupId);
            List<CourseDtoResponse> courses = this.courseRepository
                    .getCourseByGroupAndNameContaining(groupId, courseName)
                    .stream().map(CourseDtoResponse::responseDto).collect(Collectors.toList());

            return Map.of(COURSES, courses);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    private void validateGroupAndTeacher(CourseDtoRequest dto){
        this.groupService.getById(dto.getGroupId());
        if(dto.getTeacherId() != null)  
            this.userService.getById(dto.getTeacherId());
    }

    private UserEntity studentExist(Long studentId) throws NoResultException{
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this.userService.getById(studentId).get("user"), UserEntity.class);
    }
}
