package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;

import com.cripto.project.domain.dao.IQualificationDao;
import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;
import com.cripto.project.domain.entities.QualificationEntity;
import com.cripto.project.domain.services.QualificattionService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;

@Service
public class QualificationServiceImpl implements QualificattionService {

    private static final String QUALIFICATION = "qualification";
    private static final String QUALIFICATIONS = "qualifications";

    private final IQualificationDao qualificationRepository;

    QualificationServiceImpl(IQualificationDao qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    @Override
    public final Map<String, String> delete(Long qualificationId) {
        try {
            return Map.of("message", "Qualification deleted successfully");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, List<QualificationsByStudentDtoResponse>> getAll() {
        return Map.of(QUALIFICATIONS, Collections.emptyList());
    }

    @Override
    public Map<String,QualificationsByStudentDtoResponse> getById(Long qualificationId) {
        try {
            QualificationEntity qualification = this.qualificationRepository.getById(qualificationId);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);
            
            return Map.of(QUALIFICATION, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String,QualificationsByStudentDtoResponse> register(QualificationDtoRequest dto) {
         try {
            ModelMapper modelMapper = new ModelMapper();
            QualificationEntity entity = modelMapper.map(dto, QualificationEntity.class);

            QualificationEntity qualification = this.qualificationRepository.register(entity);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);

            return Map.of(QUALIFICATION, response);
        } catch(EntityExistsException e){
            throw new EntityExistsException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String,QualificationsByStudentDtoResponse> update(Long qualificationId, QualificationDtoRequest dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            QualificationEntity entity = modelMapper.map(dto, QualificationEntity.class);

            QualificationEntity qualification = this.qualificationRepository.update(qualificationId, entity);
            QualificationsByStudentDtoResponse response = QualificationsByStudentDtoResponse.responseDto(qualification);

            return Map.of(QUALIFICATION, response);
        } catch(EntityExistsException e){
            throw new EntityExistsException(e.getMessage());
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, List<QualificationsByStudentDtoResponse>> getQualificationsByStudent(Long studentId) {
        try {
            List<QualificationsByStudentDtoResponse> qualifications = this.qualificationRepository.getQualificationsByStudent(studentId)
                .stream().map(QualificationsByStudentDtoResponse::responseDto).collect(Collectors.toList());
        
            return Map.of(QUALIFICATIONS, qualifications);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, List<QualificationsByCourseDtoResponse>> getQualificationsByCourse(Long courseId) {
        try {
            List<QualificationsByCourseDtoResponse> qualifications = this.qualificationRepository.getQualificationsByCourse(courseId)
                .stream().map(QualificationsByCourseDtoResponse::responseDto).collect(Collectors.toList());
            
            return Map.of(QUALIFICATIONS, qualifications);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
