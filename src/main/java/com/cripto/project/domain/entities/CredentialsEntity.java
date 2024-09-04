package com.cripto.project.domain.entities;

import com.cripto.project.utils.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "credentials")
public class CredentialsEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "account_no_locked", nullable = false)
    private boolean accountNoLocked ;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "role_name", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;
}
