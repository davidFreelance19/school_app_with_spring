package com.cripto.project.domain.services;


import java.util.Map;

import com.cripto.project.domain.dtos.consumes.UserDtoRequest;
import com.cripto.project.domain.dtos.produces.user.UserDtoResponse;
import com.cripto.project.utils.RoleEnum;

public interface IUserService extends ICrudService<UserDtoResponse, UserDtoRequest>{
    public Map<String, UserDtoResponse> register(UserDtoRequest request, RoleEnum role);
}
