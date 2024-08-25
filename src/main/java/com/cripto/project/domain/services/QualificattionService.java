package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByCourseDtoResponse;
import com.cripto.project.domain.dtos.produces.qualification.QualificationsByStudentDtoResponse;

public interface QualificattionService extends CrudService<QualificationsByStudentDtoResponse, QualificationDtoRequest>{

    public Map<String, List<QualificationsByStudentDtoResponse>> getQualificationsByStudent(Long studentId);
    
    public Map<String, List<QualificationsByCourseDtoResponse>> getQualificationsByCourse(Long courseId);

}
