package com.example.springbootbackend.user.repository;

import com.example.springbootbackend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE (:email IS NULL OR u.email LIKE %:email%) AND u.id <> :userId")
    Page<User> findAllUsersExceptCurrentByEmailLike(@Param("userId") Long userId, @Param("email") String email, Pageable pageable);

    // Metodo per cercare le mail degli utenti per email con un pattern
    @Query("SELECT u.email FROM User u WHERE u.email LIKE %:email%")
    List<String> findUserNamesByEmailLike(String email);
}
