package com.nexusflow.services;

import java.util.List;
import java.util.Optional;
import com.nexusflow.entities.User;

public interface UserService {

    //some custom methods
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUserById(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
}
