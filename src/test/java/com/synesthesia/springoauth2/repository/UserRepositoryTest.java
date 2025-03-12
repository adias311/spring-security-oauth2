package com.synesthesia.springoauth2.repository;

import com.synesthesia.springoauth2.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Nested
    @Tag("unit-test")
    class UserRepositoryUnitTest {

        @Test
        void testFindFirstByUsername() {
            User user = User.builder()
                        .username("john doe")
                        .password("password")
                        .build();

            userRepository.save(user);

            Optional<User> savedUser = userRepository.findFirstByUsername("john doe");

            assertTrue(savedUser.isPresent());
            assertTrue(savedUser.get().getId() > 0);
            assertEquals(user.getUsername(), savedUser.get().getUsername());
            assertEquals(user.getPassword(), savedUser.get().getPassword());
        }

        @Test
        void testSaveDuplicateUser() {

            User user = User.builder().username("john doe").build();
            User user2 = User.builder().username("john doe").build();

            userRepository.save(user);

            assertThrows(DataIntegrityViolationException.class,
                    () -> userRepository.save(user2)
            );

        }

    }

    @Nested
    @Tag("integration-test")
    class UserRepositoryIntegrationTest {

    }

}