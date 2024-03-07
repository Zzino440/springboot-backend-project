package com.example.springbootbackend.user.service;

import com.example.springbootbackend.exceptions.MyProjectError;
import com.example.springbootbackend.exceptions.MyProjectException;
import com.example.springbootbackend.user.exception.ResourceNotFoundException;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.springbootbackend.exceptions.MyProjectError.EMAIL_ALREADY_IN_USE;
import static com.example.springbootbackend.exceptions.MyProjectError.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;

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
    public Page<User> getAllUsersExceptCurrent(Long id, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllUsersExceptCurrentByEmailLike(id, email, pageable);
    }

    public User createUser(User user) {
        // Controlla se esiste già un utente con la stessa email
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            // Crea una risposta di errore personalizzata
            throw new MyProjectException(USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
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

    @Override
    public List<String> searchUserNamesByEmail(String email) {
        return userRepository.findUserNamesByEmailLike(email);
    }


    //utility methods
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
    }

    public void checkIfEmailExistsElsewhere(Long currentUserId, String newEmail) throws MyProjectException {
        userRepository.findByEmail(newEmail)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(currentUserId)) {
                        throw new MyProjectException(EMAIL_ALREADY_IN_USE, "Email " + newEmail + " is already in use");
                    }
                });
    }

}
