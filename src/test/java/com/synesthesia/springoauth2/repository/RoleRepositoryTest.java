package com.synesthesia.springoauth2.repository;

import com.synesthesia.springoauth2.entity.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest extends RepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Nested
    @Tag("unit-test")
    class RoleRepositoryUnitTest {

        @Test
        void testFindFirstByName() {

            Role role = Role.builder().name("USER").build();
            roleRepository.save(role);

            Optional<Role> userRole = roleRepository.findFirstByName("USER");

            assertTrue(userRole.isPresent());
            assertTrue(userRole.get().getId() > 0);
            assertEquals("USER" , userRole.get().getName());

        }

        @Test
        void testSaveDuplicateRole() {

            Role role = Role.builder().name("USER").build();
            Role role2 = Role.builder().name("USER").build();

            roleRepository.save(role);

            assertThrows(DataIntegrityViolationException.class,
                  () -> roleRepository.save(role2)
            );

        }

    }

    @Nested
    @Tag("integration-test")
    class RoleRepositoryIntegrationTest {

    }

}