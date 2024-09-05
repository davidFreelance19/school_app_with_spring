package com.cripto.project.presentation.config.security.aspects.auth;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cripto.project.utils.JwtUtil;

@Aspect
@Component
public class VerifyUserByTokenAspect {
    
    private JwtUtil jwtUtil;

    public VerifyUserByTokenAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Before("@annotation(RequireVerifyUserByToken) && args(token,..)")
    public void checkQualificationOwnership(String token) throws AccessDeniedException {
        try {
            jwtUtil.validateToken(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }
}
