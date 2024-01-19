package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.exception.ResourceNotFoundException;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    //injections
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    //methods implemented
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.findById(id);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        var user = this.findById(id);

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        // Aggiorna la password solo se è stata fornita
        Optional.ofNullable(userDetails.getPassword())
                .filter(password -> !password.isEmpty())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.findById(id);
        userRepository.delete(user);
    }


    //utility methods
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
    }
}
