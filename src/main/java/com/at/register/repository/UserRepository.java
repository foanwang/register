package com.at.register.repository;


import com.at.register.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(String UserId);

    Optional<User> findByEmail(String Email);

    User findByUsername(String Username);

    Boolean existsByEmail(String email);
}
