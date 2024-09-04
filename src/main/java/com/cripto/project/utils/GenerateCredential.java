package com.cripto.project.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.cripto.project.domain.entities.CredentialsEntity;
import com.cripto.project.domain.entities.UserEntity;
import java.util.Random;

public class GenerateCredential {

    private final PasswordEncoder passwordEncoder;
    private final Random rand = new Random();
    private String password;

    public GenerateCredential(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String getPassword() {
        return password;
    }
    
    public final CredentialsEntity generateCredentials(UserEntity user, RoleEnum role){
        password = generatePassword(user.getName());

        return CredentialsEntity
                    .builder()
                    .user(user)
                    .roleEnum(role)
                    .username(user.getEmail())
                    .password(passwordHash(password))
                    .accountNoLocked(true)
                    .isEnabled(false)
                    .build();
    }

    private final String passwordHash(String password){
        return passwordEncoder.encode(password);
    }

    private final String generatePassword(String name){
        return name +  "" + rand.nextInt(10, 100) + rand.nextInt(10, 100);
    }
    
}
