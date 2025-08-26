package com.nexusflow.config;


import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nexusflow.services.impl.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

    // login user using in memory authentication (only for testing purpose)
    // @Bean
    // public UserDetailsService userDetailsService(){
    //     UserDetails user1 = User
    //     .withDefaultPasswordEncoder()
    //     .username("admin")
    //     .password("admin")
    //     .roles("ADMIN", "USER")
    //     .build();

    //      UserDetails user2= User
    //      .withDefaultPasswordEncoder()
    //     .username("ram")
    //     .password("ram")
    //     // .roles(null)
    //     .build();
    //     InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1, user2);
    //     return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService; 

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
