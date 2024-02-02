package com.example.springbootbackend.user.repository;

import com.example.springbootbackend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByIdNot(Long userId, Pageable pageable);

    // Metodo per cercare le mail degli utenti per email con un pattern
    @Query("SELECT u.email FROM User u WHERE u.email LIKE %:email%")
    List<String> findUserNamesByEmailLike(String email);
}
