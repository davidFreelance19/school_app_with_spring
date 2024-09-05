package com.cripto.project.datasource.services;

import org.springframework.security.core.Authentication;

import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;
import com.cripto.project.domain.entities.CredentialsEntity;
import com.cripto.project.domain.services.IAuthService;
import com.cripto.project.utils.JwtUtil;

import jakarta.persistence.NoResultException;

import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final String MESSAGE = "message";

    private JwtUtil jwtUtil;

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private ICredentialsDao credentialsDao;

    AuthServiceImpl(
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsServiceImpl,
            PasswordEncoder passwordEncoder,
            ICredentialsDao credentialsDao,
            EmailService emailService) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.credentialsDao = credentialsDao;
        this.emailService = emailService;
    }

    public Map<String, String> login(LoginDtoRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateToken(authentication);

        return Map.of("token", accessToken);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);

        try {
            if (!passwordEncoder.matches(password, userDetails.getPassword()))
                throw new BadCredentialsException("Invalid username or password");

            if (!userDetails.isEnabled())
                throw new BadCredentialsException(
                        "Account not verified. Please check your email to verify your account");

            return new UsernamePasswordAuthenticationToken(
                    username,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public Map<String, String> verifyUser(String token) {
        DecodedJWT tokenDecoded = jwtUtil.validateToken(token);
        String username = tokenDecoded.getSubject();
        CredentialsEntity credential = this.credentialsDao.getCredentialsByUsername(username);

        if (credential.isEnabled())
            return Map.of(MESSAGE, "Account already verified");

        this.credentialsDao.verifyUser(username);
        return Map.of(MESSAGE, "Account verfied successfully");
    }

    @Override
    public Map<String, String> recupereAccount(String username) {
        try {
            CredentialsEntity credential = this.credentialsDao.getCredentialsByUsername(username);
            if (!credential.isEnabled())
                return Map.of(MESSAGE, "Account does not verfied");

            String token = jwtUtil.genereteTokenVerifyUser(username);
            this.emailService.sendRecupereAccountEmail(username, token);

            return Map.of(MESSAGE, "Check your email and follow the instructions");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public Map<String, String> resetPassword(String token, String password) {
        DecodedJWT tokenDecoded = jwtUtil.validateToken(token);
        String username = tokenDecoded.getSubject();

        String passwordHash = passwordEncoder.encode(password);

        this.credentialsDao.resetPassword(passwordHash, username);
        return Map.of(MESSAGE, "Password changed successfully");
    }

}
