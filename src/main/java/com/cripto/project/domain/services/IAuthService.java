package com.cripto.project.domain.services;

import java.util.Map;

import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;

public interface IAuthService {

    public Map<String, String> login (LoginDtoRequest login);
    
}
