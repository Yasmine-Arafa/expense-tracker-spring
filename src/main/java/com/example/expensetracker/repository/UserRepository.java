package com.example.expensetracker.repository;

import com.example.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // the CRUD operations already provided by the JpaRepository

   // The following methods name follow a specific pattern.
   // Spring Data JPA will automatically generate the query based on the method name.
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // for custom queries, we can use JPQL by @Query annotation
}
