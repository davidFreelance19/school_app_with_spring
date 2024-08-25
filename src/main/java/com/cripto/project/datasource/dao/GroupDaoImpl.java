package com.cripto.project.datasource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cripto.project.domain.dao.IGroupDao;
import com.cripto.project.domain.entities.GroupEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class GroupDaoImpl implements IGroupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public GroupEntity register(GroupEntity entity)  {
        this.em.persist(entity);
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupEntity> getAll() {
        List<GroupEntity> groups = new ArrayList<>();
        this.em.createQuery("SELECT g FROM GroupEntity g", GroupEntity.class)
                .getResultList().forEach(groups::add);

        return groups;
    }

    @Override
    @Transactional(readOnly = true)
    public GroupEntity getById(Long id) throws NoResultException {
        return Optional.ofNullable(this.em.find(GroupEntity.class, id))
                .orElseThrow(() -> new NoResultException("Group not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupEntity> getGroupByNameContaining(String name) {
        List<GroupEntity> groups = new ArrayList<>();
        this.em
                .createQuery("SELECT g FROM GroupEntity g WHERE g.name LIKE :name", GroupEntity.class)
                .setParameter("name", "%" + name + "%")
                .getResultList().stream().forEach(groups::add);

        return groups;
    }

    @Override
    @Transactional
    public GroupEntity update(Long id, GroupEntity entity) throws NoResultException {
        GroupEntity groupToUpdate = this.getById(id);

        groupToUpdate.setName(entity.getName());
        this.em.merge(groupToUpdate);

        return groupToUpdate;
    }


    @Override
    @Transactional
    public void delete(Long id) throws NoResultException {
        GroupEntity groupToDelete = this.getById(id);
        this.em.remove(groupToDelete);
    }
}
