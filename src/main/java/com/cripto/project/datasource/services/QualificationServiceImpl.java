package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.ICourseDao;
import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.dao.IQualificationDao;
import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.entities.CourseEntity;
import com.cripto.project.domain.entities.CredentialsEntity;
import com.cripto.project.domain.entities.QualificationEntity;
import com.cripto.project.domain.services.IQualificattionService;
import com.cripto.project.domain.services.IUserService;

import jakarta.persistence.NoResultException;

@Service
public class QualificationServiceImpl implements IQualificattionService {

    private static final String QUALIFICATION = "qualification";
    private static final String QUALIFICATIONS = "qualifications";

    private final IQualificationDao qualificationDao;
    private final ICourseDao courseDao;
    private final IUserService userService;
    private final ICredentialsDao credentialsDao;

    QualificationServiceImpl(
            IQualificationDao qualificationDao,
            ICourseDao courseDao,
            IUserService userService,
            ICredentialsDao credentialsDao
    ) {
        this.qualificationDao = qualificationDao;
        this.courseDao = courseDao;
        this.userService = userService;
        this.credentialsDao = credentialsDao;
    }

    @Override
    public final Map<String, String> delete(Long qualificationId) {
        try {
            this.qualificationDao.delete(qualificationId);
            return Map.of("message", "Qualification deleted successfully");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public Map<String, QualificationsByStudentDtoResponse> getById(Long qualificationId) {
        try {
            QualificationEntity qualification = this.qualificationDao.getById(qualificationId);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);

            return Map.of(QUALIFICATION, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, QualificationsByStudentDtoResponse> register(QualificationDtoRequest dto) {
        try {
            this.courseDao.getById(dto.getCourseId());
            this.userService.getById(dto.getStudentId());

            ModelMapper modelMapper = new ModelMapper();
            QualificationEntity entity = modelMapper.map(dto, QualificationEntity.class);

            QualificationEntity qualification = this.qualificationDao.register(entity);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);

            return Map.of(QUALIFICATION, response);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Qualification already registered");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, QualificationsByStudentDtoResponse> update(Long qualificationId,
            QualificationDtoRequest dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            QualificationEntity entity = modelMapper.map(dto, QualificationEntity.class);

            QualificationEntity qualification = this.qualificationDao.update(qualificationId, entity);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);

            return Map.of(QUALIFICATION, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, List<QualificationsByStudentDtoResponse>> getQualificationsByStudent(String username) {
        try {
            CredentialsEntity credential = this.credentialsDao.getCredentialsByUsername(username);
            List<QualificationsByStudentDtoResponse> qualifications = credential.getUser().getQualifications()
                    .stream().map(QualificationsByStudentDtoResponse::responseDto).collect(Collectors.toList());

            return Map.of(QUALIFICATIONS, qualifications);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, List<QualificationsByCourseDtoResponse>> getQualificationsByCourse(Long courseId) {
        try {
            CourseEntity course = this.courseDao.getById(courseId);

            List<QualificationsByCourseDtoResponse> qualifications = course.getQualifications()
                    .stream().map(QualificationsByCourseDtoResponse::responseDto).collect(Collectors.toList());

            return Map.of(QUALIFICATIONS, qualifications);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public boolean isOwnerOfQualification(Long qualificationId, String username) {
        try {
            QualificationEntity qualification = qualificationDao.getById(qualificationId);

            return qualification.getCourse().getTeacher().getEmail().equals(username);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public boolean isOwnerOfCourse(Long courseId, String username) {
        try {
            CourseEntity course = courseDao.getById(courseId);

            return course.getTeacher().getEmail().equals(username);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }
}
