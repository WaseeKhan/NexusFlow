package com.nexusflow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexusflow.entities.User;
import com.nexusflow.repositories.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(
        @RequestParam("token") String token){
            logger.info("Token received: {}", token);

            User user = userRepository.findByEmailToken(token).orElse(null);
            if (user != null) {

                if(user.getEmailToken().equals(token)){
                    user.setEmailVerified(true);
                    user.setEnabled(true); 
                    userRepository.save(user);
                    return "success_page";
                }
                return "error_page";
            }
        return "error_page";
    }

}
