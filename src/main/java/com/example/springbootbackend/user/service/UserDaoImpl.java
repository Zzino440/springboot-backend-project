package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.exception.ResourceNotFoundException;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
    public List<User> getAllUsersExceptCurrent(Long id) {
        return userRepository.findByIdNot(id);
    }

    public User createUser(User user) {
        // Controlla se esiste già un utente con la stessa email
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            // Crea una risposta di errore personalizzata
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un utente con questa email esiste già."
            );
        }

        // Se non esiste, procedi con la creazione dell'utente
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.findById(id);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        var user = this.findById(id);

        //check sulla mail di tutti gli utenti tranne quello corrente
        this.checkIfEmailExistsElsewhere(id, userDetails.getEmail());

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

    @Override
    public Boolean checkEmail(String email) {
        // Controlla se esiste già un utente con la stessa email
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }


    //utility methods
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
    }

    public void checkIfEmailExistsElsewhere(Long currentUserId, String newEmail) {
        userRepository.findByEmail(newEmail)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(currentUserId)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email già in uso");
                    }
                });
    }


}
