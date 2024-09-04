package com.cripto.project.domain.dao;

import com.cripto.project.domain.entities.CredentialsEntity;

public interface ICredentialsDao {

    public CredentialsEntity getCredentialsByUsername(String username);

    public void registerCredential(CredentialsEntity credential);

}
