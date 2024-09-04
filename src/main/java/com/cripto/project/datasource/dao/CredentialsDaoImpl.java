package com.cripto.project.datasource.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.entities.CredentialsEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class CredentialsDaoImpl implements ICredentialsDao{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public CredentialsEntity getCredentialsByUsername(String username) throws NoResultException {
        try {
            return this.em
            .createQuery("SELECT c FROM CredentialsEntity c WHERE c.username = :username", CredentialsEntity.class)
            .setParameter("username", username)
            .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("User not found");
        }
    }

    @Override
    @Transactional
    public void registerCredential(CredentialsEntity credential) {
        this.em.persist(credential);
    }
    
}
