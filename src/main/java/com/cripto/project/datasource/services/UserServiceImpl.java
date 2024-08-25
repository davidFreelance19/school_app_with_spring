package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.cripto.project.domain.dao.IUserDao;
import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.domain.entities.UserEntity;
import com.cripto.project.domain.services.UserService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;

@Service
public class UserServiceImpl implements UserService{

    private static final String USER = "user";
    private static final String USERS = "users";

    private final IUserDao userRepository;

    UserServiceImpl(IUserDao userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public final Map<String, UserDtoResponse> register(UserDtoRequest user) {
        try {
            ModelMapper mapper = new ModelMapper();
            UserEntity entity = mapper.map(user, UserEntity.class);

            UserDtoResponse response = UserDtoResponse.responseDto(this.userRepository.register(entity));
            return Map.of(USER, response);
        } catch(EntityExistsException e){
            throw new EntityExistsException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, List<UserDtoResponse>> getAll() {
        try {
            List<UserDtoResponse> users = this.userRepository.getAll().stream()
                .map(UserDtoResponse::responseDto).collect(Collectors.toList());

            return Map.of(USERS, users);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, UserDtoResponse> getById(Long userId) {
        try {
            UserDtoResponse response =  UserDtoResponse.responseDto(this.userRepository.getById(userId));
            return Map.of(USER, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    
    @Override
    public final Map<String, UserDtoResponse> update(Long userId, UserDtoRequest dto) {
        try {
            ModelMapper mapper = new ModelMapper();
            UserEntity entity = mapper.map(dto, UserEntity.class);

            UserDtoResponse response = UserDtoResponse.responseDto(this.userRepository.update(userId, entity));
            return Map.of(USER, response);
        } catch(EntityExistsException e){
            throw new EntityExistsException(e.getMessage());
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public final Map<String, String> delete(Long userId) {
        try {
            return Map.of("message", "User deleted sucessfully"); 
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, GlobalErrorsMessage.INTERNAL_SERVER_ERROR);
        }
    }
    
}
