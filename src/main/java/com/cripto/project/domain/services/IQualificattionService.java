package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;

public interface IQualificattionService extends ICrudService<QualificationsByStudentDtoResponse, QualificationDtoRequest>{

    public Map<String, List<QualificationsByStudentDtoResponse>> getQualificationsByStudent(String username);
    
    public Map<String, List<QualificationsByCourseDtoResponse>> getQualificationsByCourse(Long courseId);

    public boolean isOwnerOfQualification(Long qualificationId, String username);

    public boolean isOwnerOfCourse(Long courseId, String username);
}
