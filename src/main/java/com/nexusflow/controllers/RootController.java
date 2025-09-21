package com.nexusflow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nexusflow.entities.User;
import com.nexusflow.helpers.Helper;
import com.nexusflow.services.UserService;

@ControllerAdvice
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService userService;

        @ModelAttribute
        public void addLoggedInUserInformation(Model model, Authentication authentication){
            if (authentication ==null) {
                // System.out.println("No authenticated user, returing null");
                // logger.info("You're viewing application as guest user, please login and explore more.");
                return;
                
            }
        System.out.println("addLoggedInUserInformation to the model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User loggedIn: {}: ", username);
        User user = userService.getUserByEmail(username);

            System.out.println(user);
            System.out.println("Logged in user found: " + user.getEmail());
            System.out.println(user.getFirstName());
            System.out.println(user.getEmail());
           
            model.addAttribute("loggedInUser", user);
        
       
    }



}
