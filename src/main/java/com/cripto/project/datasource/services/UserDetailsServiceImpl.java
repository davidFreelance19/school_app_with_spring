package com.cripto.project.datasource.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.ICredentialsDao;
import com.cripto.project.domain.entities.CredentialsEntity;

import jakarta.persistence.NoResultException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final ICredentialsDao credentialsDao;

    UserDetailsServiceImpl(ICredentialsDao credentialsDao){
        this.credentialsDao = credentialsDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
          CredentialsEntity credentials = this.credentialsDao.getCredentialsByUsername(username);
          List<SimpleGrantedAuthority> autorityList = List.of(
                new SimpleGrantedAuthority("ROLE_".concat(credentials.getRoleEnum().name()))
          );
          
          return new User(
                  credentials.getUsername(),
                  credentials.getPassword(),
                  credentials.isEnabled(),
                  true,
                  true,
                  credentials.isAccountNoLocked(),
                  autorityList
        );
        
        } catch (NoResultException e) {
                throw new UsernameNotFoundException(e.getMessage());
        }
    }
}