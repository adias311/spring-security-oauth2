package com.synesthesia.spring_oauth2.repository;

import com.synesthesia.spring_oauth2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findFirstByName(String name);

}
