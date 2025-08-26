package com.nexusflow.services.impl;

import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nexusflow.repositories.UserRepository;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService{

    @AutoConfigureOrder
    private UserRepository  userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with this email: "+username));
    }


     

}
