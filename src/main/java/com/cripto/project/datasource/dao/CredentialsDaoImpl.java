package com.cripto.project.datasource.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.entities.CredentialsEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class CredentialsDaoImpl implements ICredentialsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public CredentialsEntity getCredentialsByUsername(String username) throws NoResultException {
        return Optional.ofNullable(this.em.find(CredentialsEntity.class, username))
            .orElseThrow(() -> new NoResultException("Invalid username or password"));
    }

    @Override
    @Transactional
    public void registerCredential(CredentialsEntity credential) {
        this.em.persist(credential);
    }

    @Override
    @Transactional
    public void verifyUser(String username) {
        this.em.createQuery("UPDATE CredentialsEntity c SET c.isEnabled = :enable WHERE c.username = :username")
                .setParameter("username", username)
                .setParameter("enable", true)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void resetPassword(String newPassword, String username) {
        this.em.createQuery("UPDATE CredentialsEntity c SET c.password = :newPassword WHERE c.username = :username")
            .setParameter("username", username)
            .setParameter("newPassword", newPassword)
            .executeUpdate();
    }

}
