package com.ridesharing.userservice.repository;

import com.ridesharing.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndTenantId(String username, String tenantId);

    Optional<User> findByEmailAndTenantId(String email, String tenantId);

    boolean existsByUsernameAndTenantId(String username, String tenantId);

    boolean existsByEmailAndTenantId(String email, String tenantId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    java.util.Optional<User> findByIdAndTenantId(Long id, String tenantId);

    void deleteByIdAndTenantId(Long id, String tenantId);
} 