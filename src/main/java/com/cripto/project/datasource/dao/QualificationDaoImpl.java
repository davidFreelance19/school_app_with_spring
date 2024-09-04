package com.cripto.project.datasource.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.IQualificationDao;
import com.cripto.project.domain.entities.QualificationEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;


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
    public QualificationEntity getById(Long id) throws NoResultException{
        return Optional.ofNullable(this.em.find(QualificationEntity.class, id))
            .orElseThrow(()-> new NoResultException("Qualification not found"));
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

}
