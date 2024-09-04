package com.cripto.project.datasource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cripto.project.domain.dao.ICourseDao;
import com.cripto.project.domain.entities.CourseEntity;
import com.cripto.project.domain.entities.UserEntity;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class CourseDaoImpl implements ICourseDao {

    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional
    public CourseEntity register(CourseEntity entity){
        this.em.persist(entity);
        this.refresh(entity);
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseEntity> getAll() {
        return this.em.createQuery("SELECT c FROM CourseEntity c", CourseEntity.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseEntity getById(Long id) throws NoResultException {
        return Optional.ofNullable(this.em.find(CourseEntity.class, id))
            .orElseThrow(()-> new NoResultException("Course not found"));
    }

    @Override
    @Transactional
    public CourseEntity update(Long id, CourseEntity entity) throws NoResultException {
        CourseEntity courseExist = this.getById(id);

        courseExist.setGroup(entity.getGroup());
        courseExist.setName(entity.getName());
        courseExist.setTeacher(entity.getTeacher());

        this.em.merge(courseExist);
        this.refresh(courseExist);
        return courseExist;
    }

    @Override
    @Transactional
    public void delete(Long id) throws NoResultException {
        CourseEntity courseToDelete = getById(id);
        this.em.remove(courseToDelete);
    }

    @Override
    @Transactional
    public void addStudentToCourse(Long id, UserEntity student) throws EntityExistsException{
        CourseEntity course = this.getById(id);

        boolean isStudentRegistered = course.getStudents().stream().anyMatch(u -> u.getId().equals(student.getId()));

        if (isStudentRegistered) 
            throw new EntityExistsException("User already registered in this course");

        course.getStudents().add(student);
        this.em.merge(course);
    }

    @Override
    @Transactional
    public void deleteStudentToCourse(Long id,  UserEntity student) throws NoResultException{
        CourseEntity course = this.getById(id);
        Optional<UserEntity> isStudentRegistered = course.getStudents().stream().filter(s -> s.getId().equals(student.getId())).findFirst();

        if (!isStudentRegistered.isPresent()) 
            throw new NoResultException("User not registered in this course");

        course.getStudents().remove(isStudentRegistered.get());
        this.em.merge(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseEntity> getCourseByGroupAndNameContaining(Long groupId, String courseName) {
        List<CourseEntity> courses = new ArrayList<>();
        this.em.createQuery(
            "SELECT c FROM CourseEntity c WHERE c.group.id = :id AND c.name LIKE :name",
            CourseEntity.class)
            .setParameter("id", groupId)
            .setParameter("name", "%" + courseName + "%")
            .getResultList()
            .stream().forEach(courses::add);
        
        return courses;
    }

    public void refresh(CourseEntity entity) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                em.refresh(entity);
            }
        });
    }
}
