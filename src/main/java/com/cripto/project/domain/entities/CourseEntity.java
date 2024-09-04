package com.cripto.project.domain.entities;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    name = "courses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "group_id"})
    }
)
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = GroupEntity.class)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "teacher_id")
    private UserEntity teacher;

    @ManyToMany(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_students", // Nombre de la tabla pivote
            joinColumns = @JoinColumn(name = "course_id", nullable = false), 
            inverseJoinColumns = @JoinColumn(name = "student_id", nullable = false) 
    )
    private List<UserEntity> students;

    @OneToMany(targetEntity = QualificationEntity.class, mappedBy ="course", fetch = FetchType.LAZY)
    private List<QualificationEntity> qualifications;
}
