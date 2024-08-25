package com.cripto.project.domain.dao;

import java.util.List;

import com.cripto.project.domain.entities.QualificationEntity;

public interface IQualificationDao extends ICrudDao<QualificationEntity> {

    public List<QualificationEntity> getQualificationsByCourse(Long courseId);

    public List<QualificationEntity> getQualificationsByStudent(Long studentId);
    
}
