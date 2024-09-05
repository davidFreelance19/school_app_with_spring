package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.dao.IUserDao;
import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.entities.CredentialsEntity;
import com.cripto.project.domain.entities.UserEntity;
import com.cripto.project.domain.services.IUserService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;
import com.cripto.project.utils.RoleEnum;
import com.cripto.project.utils.GenerateCredential;
import com.cripto.project.utils.JwtUtil;

import jakarta.persistence.NoResultException;

@Service
public class UserServiceImpl implements IUserService {

    private static final String USER = "user";
    private static final String USERS = "users";

    private final IUserDao userDao;
    private final ICredentialsDao credentialsDao;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private final EmailService emailService;

    UserServiceImpl(
        IUserDao userDao, 
        ICredentialsDao credentialsDao, 
        PasswordEncoder passwordEncoder,
        EmailService emailService,
        JwtUtil jwtUtil
    ) {
        this.userDao = userDao;
        this.credentialsDao = credentialsDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public final Map<String, UserDtoResponse> register(UserDtoRequest user, RoleEnum role) {
        try {
            ModelMapper mapper = new ModelMapper();
            UserEntity entity = mapper.map(user, UserEntity.class);
            UserEntity newUser = this.userDao.register(entity);

            GenerateCredential newCredential = new GenerateCredential(this.passwordEncoder);
            CredentialsEntity credential = newCredential.generateCredentials(newUser, role);
            this.credentialsDao.registerCredential(credential);
            
            String tokenVerifyUser = this.jwtUtil.genereteTokenVerifyUser(credential.getUsername());
            this.emailService.sendWelcomeAppEmail(credential, newCredential.getPassword(), tokenVerifyUser);

            UserDtoResponse response = UserDtoResponse.responseDto(newUser);
            return Map.of(USER, response);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User already registered");
        }
    }

    @Override
    public final Map<String, List<UserDtoResponse>> getAll() {
        List<UserDtoResponse> users = this.userDao.getAll().stream()
                .map(UserDtoResponse::responseDto).collect(Collectors.toList());

        return Map.of(USERS, users);
    }

    @Override
    public final Map<String, UserDtoResponse> getById(Long userId) {
        try {
            UserDtoResponse response = UserDtoResponse.responseDto(this.userDao.getById(userId));
            return Map.of(USER, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, UserDtoResponse> update(Long userId, UserDtoRequest dto) {
        try {
            ModelMapper mapper = new ModelMapper();
            UserEntity entity = mapper.map(dto, UserEntity.class);

            UserDtoResponse response = UserDtoResponse.responseDto(this.userDao.update(userId, entity));
            return Map.of(USER, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User already registered");
        }
    }

    @Override
    public final Map<String, String> delete(Long userId) {
        try {
            this.userDao.delete(userId);
            return Map.of("message", "User deleted sucessfully");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION);
        }
    }

    @Override
    public Map<String, String> resetPassword(String username, String password) {
        String passwordHash = passwordEncoder.encode(password);

        this.credentialsDao.resetPassword(passwordHash, username);
        return Map.of("MESSAGE", "Password changed successfully");
    }
}
