package com.cripto.project.domain.services;


import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;

public interface UserService extends CrudService<UserDtoResponse, UserDtoRequest>{
    
}
