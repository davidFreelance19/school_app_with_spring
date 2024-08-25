package com.cripto.project.datasource.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.IQualificationDao;
import com.cripto.project.domain.entities.QualificationEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;

@Repository
public class QualificationDaoImpl implements IQualificationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void delete(Long id) {
        QualificationEntity qualificationToDelete = this.getById(id);
        this.em.remove(qualificationToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public QualificationEntity getById(Long id) {
        return this.em.find(QualificationEntity.class, id);
    }

    @Override
    @Transactional
    public QualificationEntity register(QualificationEntity entity) {
        this.em.persist(entity);
        this.em.refresh(entity);
        return entity;
    }

    @Override
    @Transactional
    public QualificationEntity update(Long id, QualificationEntity entity) {
        QualificationEntity qualification = this.getById(id);
        qualification.setQualification(entity.getQualification());

        this.em.merge(qualification);
        return qualification;
    }

    @Override
    public List<QualificationEntity> getAll() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationEntity> getQualificationsByCourse(Long courseId) {
        return this.em
                .createQuery("SELECT q FROM QualificationEntity q WHERE q.course.id = :id", QualificationEntity.class)
                .setParameter("id", courseId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationEntity> getQualificationsByStudent(Long studentId) {
        return this.em
                .createQuery("SELECT q FROM QualificationEntity q WHERE q.student.id = :id", QualificationEntity.class)
                .setParameter("id", studentId)
                .getResultList();
    }

}
