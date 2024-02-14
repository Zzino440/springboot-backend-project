package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.enums.Role;
import com.example.springbootbackend.user.mapper.UserMapper;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    private final UserRepository userRepository;

    //test injections
    private static final String[] ROLES = {"USER", "ADMIN"};
    private static final Random RANDOM = new Random();
    private static final String PASSWORD_ENCRYPTED = "$2a$10$Qgj2ztQ6VDsNzNZVHYXNIOpTz5Am99V6BIEPndbMjT/ljE0kDjNvG";


    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> getAllUsersExceptCurrent(Long id, String email, int page, int size) {
        Page<User> usersPage = userDao.getAllUsersExceptCurrent(id, email, page, size);
        return usersPage.map(UserMapper::toUserDTO);
    }


    public User createUser(User user) {
        return userDao.createUser(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userDao.getUserById(id);
        return UserMapper.toUserDTO(user);
    }

    public User updateUser(Long id, User user) {
        log.info("Updated User {}", user);
        return userDao.updateUser(id, user);
    }

    public Map<String, Boolean> deleteUser(Long id) {
        userDao.deleteUser(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public List<String> searchUserNamesByEmail(String email) {
        return userDao.searchUserNamesByEmail(email);
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
