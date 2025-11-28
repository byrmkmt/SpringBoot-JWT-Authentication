package com.security.session.repository;

import com.security.session.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByUsername(String username);

    public Optional<User> findByUsername(String username);

}