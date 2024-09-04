package com.cripto.project.datasource.services;

import org.springframework.security.core.Authentication;

import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dtos.consumes.LoginDtoRequest;
import com.cripto.project.domain.services.IAuthService;
import com.cripto.project.utils.JwtUtil;

@Service
public class AuthServiceImpl implements IAuthService{

    private JwtUtil jwtUtil;

    private UserDetailsServiceImpl userDetailsServiceImpl;

    private PasswordEncoder passwordEncoder;

    AuthServiceImpl(
        JwtUtil jwtUtil, 
        UserDetailsServiceImpl userDetailsServiceImpl, 
        PasswordEncoder passwordEncoder
    ){
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(
            username, 
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );
    }
}
