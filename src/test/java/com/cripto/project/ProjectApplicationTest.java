package com.cripto.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.cripto.project.datasource.dao.GroupDaoImpl;

@ActiveProfiles("test")
@SpringBootTest
class ProjectApplicationTest {

    @Autowired
    GroupDaoImpl groupRepository;

    @Test
    void contextLoads() {
       
    }
}