package com.elgupo.elguposerver.database.repositories;

import org.springframework.data.repository.CrudRepository;
import com.elgupo.elguposerver.database.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    Optional<User> findById(Integer id);
}
