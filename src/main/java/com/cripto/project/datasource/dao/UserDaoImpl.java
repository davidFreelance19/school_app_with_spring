package com.cripto.project.datasource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.IUserDao;
import com.cripto.project.domain.entities.UserEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class UserDaoImpl implements IUserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public UserEntity register(UserEntity user) {
        this.em.persist(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        List<UserEntity> users = new ArrayList<>();
        this.em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList()
            .stream().forEach(users::add);
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(Long id) throws NoResultException {
        return Optional.ofNullable(this.em.find(UserEntity.class, id))
                .orElseThrow(() -> new NoResultException("User not found"));
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
