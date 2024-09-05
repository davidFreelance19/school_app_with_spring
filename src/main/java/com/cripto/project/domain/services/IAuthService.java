package com.cripto.project.domain.services;

import java.util.Map;

import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;

public interface IAuthService {
    public Map<String, String> login (LoginDtoRequest login);
    
    public Map<String, String> verifyUser(String token);

    public Map<String, String> recupereAccount(String username);

    public Map<String, String> resetPassword(String token, String password);
}
