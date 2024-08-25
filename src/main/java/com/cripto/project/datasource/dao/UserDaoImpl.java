package com.cripto.project.datasource.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.IUserDao;
import com.cripto.project.domain.entities.UserEntity;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

@Repository
public class UserDaoImpl implements IUserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public UserEntity register(UserEntity user) throws EntityExistsException {
        try {
            this.em.persist(user);
            return user;
        } catch (PersistenceException e) {
            throw new EntityExistsException("User already registered");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return this.em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(Long id) throws NoResultException {
        UserEntity userExist = this.em.find(UserEntity.class, id);
        if (userExist == null)
            throw new NoResultException("User not found");

        return userExist;
    }

    @Override
    @Transactional
    public UserEntity update(Long id, UserEntity user) throws NoResultException {
        UserEntity userToUpdate = this.getById(id);

        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setName(user.getName());
        userToUpdate.setLastname(user.getLastname());

        this.em.merge(userToUpdate);
        return userToUpdate;
    }

    @Override
    @Transactional
    public void delete(Long id) throws NoResultException {
        UserEntity userToDelete = this.getById(id);
        this.em.remove(userToDelete);
    }
}
