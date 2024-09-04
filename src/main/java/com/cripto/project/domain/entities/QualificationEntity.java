package com.cripto.project.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "qualifications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "student_id"})
    }
)
public class QualificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer qualification;

    @ManyToOne(targetEntity = CourseEntity.class)
    @JoinColumn(name = "course_id", nullable = false, updatable = false)
    private CourseEntity course;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "student_id", nullable = false, updatable = false)
    private UserEntity student;
}
