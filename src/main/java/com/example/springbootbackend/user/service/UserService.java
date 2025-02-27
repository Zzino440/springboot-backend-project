package com.example.springbootbackend.user.service;

import com.example.springbootbackend.exceptions.MyProjectError;
import com.example.springbootbackend.exceptions.MyProjectException;
import com.example.springbootbackend.user.DTO.UserCreateUpdateDTO;
import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.enums.Role;
import com.example.springbootbackend.user.exception.ResourceNotFoundException;
import com.example.springbootbackend.user.mapper.UserMapper;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.springbootbackend.exceptions.MyProjectError.EMAIL_ALREADY_IN_USE;
import static com.example.springbootbackend.exceptions.MyProjectError.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //test injections
    private static final String[] ROLES = {"USER", "ADMIN"};
    private static final Random RANDOM = new Random();
    private static final String PASSWORD_ENCRYPTED = "$2a$10$Qgj2ztQ6VDsNzNZVHYXNIOpTz5Am99V6BIEPndbMjT/ljE0kDjNvG";


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> getAllUsersExceptCurrent(Long id, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAllUsersExceptCurrentByEmailLike(id, email, pageable);
        return usersPage.map(UserMapper::toUserDTO);
    }


    public UserDTO createUser(UserCreateUpdateDTO user) {
        // Controlla se esiste già un utente con la stessa email
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            // Crea una risposta di errore personalizzata
            throw new MyProjectException(USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        }

        User userToSave = userMapper.toUser(user);
        userRepository.save(userToSave);
        UserDTO createdUser = UserMapper.toUserDTO(userToSave);
        return createdUser;
    }

    public UserDTO getUserById(Long id) {
        User user = this.findById(id);
        return UserMapper.toUserDTO(user);
    }

    public UserDTO updateUser(Long id, UserCreateUpdateDTO userDetails) {

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

        userRepository.save(user);

        UserDTO userCreateUpdateDTO = UserMapper.toUserDTO(user);
        return userCreateUpdateDTO;
    }

    public Map<String, Boolean> deleteUser(Long id) {
        User user = this.findById(id);
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    public List<String> searchUserNamesByEmail(String email) {
        List<String> userEmails = userRepository.findUserNamesByEmailLike(email);
        return userEmails;
    }

    //utility methods
    //method used to find user by id => used in various methods (getUserById, updateUser, deleteUser, etc...)
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MyProjectException(MyProjectError.USER_NOT_FOUND, "User doesn't exist with id:" + id));
    }

    public void checkIfEmailExistsElsewhere(Long currentUserId, String newEmail) throws MyProjectException {
        userRepository.findByEmail(newEmail)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(currentUserId)) {
                        throw new MyProjectException(EMAIL_ALREADY_IN_USE, "Email " + newEmail + " is already in use");
                    }
                });
    }


    //test Method
    @Transactional
    public void generateTestUsers(int numberOfUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            User user = new User();
            user.setEmail("test" + i + "@gmail.com");
            user.setFirstName("TestName");
            user.setLastName("TestLastName");
            user.setPassword(PASSWORD_ENCRYPTED); // Usa la password criptata
            user.setRole(Role.valueOf(ROLES[RANDOM.nextInt(ROLES.length)])); //RUOLO RANDOM

            users.add(user);
        }
        userRepository.saveAll(users); // Salva tutti gli utenti in una volta per efficienza
    }

}
