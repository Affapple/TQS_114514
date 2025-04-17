package ies.carbox.api.RestAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ies.carbox.api.RestAPI.entity.User;
import java.util.Optional;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Long deleteByEmail(String email);
    // Optional<User> findById(String id);
}
