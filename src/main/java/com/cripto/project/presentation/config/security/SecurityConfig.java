package com.cripto.project.presentation.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cripto.project.presentation.config.security.filter.JwtTokenValidator;
import com.cripto.project.utils.JwtUtil;
import com.cripto.project.utils.RoleEnum;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private static final String ADMIN = RoleEnum.ADMIN.name();
    private static final String STUDENT = RoleEnum.STUDENT.name();
    private static final String TEACHER = RoleEnum.TEACHER.name();

    private static final String ROUTES_ADMIN = "/admin/**";
    private static final String ROUTES_STUDENT = "/student/**";
    private static final String ROUTES_TEACHER = "/teacher/**";

    private AuthenticationConfiguration authenticationConfiguration;

    private JwtUtil jwtUtil;

    SecurityConfig(JwtUtil jwtUtil, AuthenticationConfiguration authenticationConfiguration){
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Enpoints publicos
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/auth/**").permitAll();

                    // Enpoints privados
                    http.requestMatchers(HttpMethod.GET, ROUTES_ADMIN).hasRole(ADMIN);
                    http.requestMatchers(HttpMethod.POST, ROUTES_ADMIN).hasRole(ADMIN);
                    http.requestMatchers(HttpMethod.PUT, ROUTES_ADMIN).hasRole(ADMIN);
                    http.requestMatchers(HttpMethod.DELETE, ROUTES_ADMIN).hasRole(ADMIN);

                    http.requestMatchers(HttpMethod.GET, ROUTES_TEACHER).hasRole(TEACHER);
                    http.requestMatchers(HttpMethod.POST, ROUTES_TEACHER).hasRole(TEACHER);
                    http.requestMatchers(HttpMethod.PUT, ROUTES_TEACHER).hasRole(TEACHER);
                    http.requestMatchers(HttpMethod.DELETE, ROUTES_TEACHER).hasRole(TEACHER);

                    http.requestMatchers(HttpMethod.GET, ROUTES_STUDENT).hasRole(STUDENT);

                    // swagger
                    http.anyRequest().permitAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }


    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
