package com.nexusflow.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexusflow.entities.User;
import com.nexusflow.helpers.ResourceNotFoundException;
import com.nexusflow.repositories.UserRepository;
import com.nexusflow.services.UserService;

import jakarta.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
         user.setUserId(userId);
    
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ user.getUserId()));
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setPassword(user.getPassword());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setEnabled(user.isEnabled());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        User save = userRepository.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUserById(String id) {
        User user2 = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));
        userRepository.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepository.findById(userId).orElse(null);
        return user2==null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
       User user = userRepository.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

   

}
