package com.synesthesia.springoauth2.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
public class RepositoryTest {

    void init() {
        System.out.println("Repository Test");
    }

}
