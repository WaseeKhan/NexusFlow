package com.nexusflow.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexusflow.entities.Contact;
import com.nexusflow.entities.User;
import com.nexusflow.forms.UserForm;
import com.nexusflow.helpers.AppConstant;
import com.nexusflow.helpers.Helper;
import com.nexusflow.helpers.ResourceNotFoundException;
import com.nexusflow.services.ContactService;
import com.nexusflow.services.UserService;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;
 
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication,
     @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
            
            ){

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        
        Page<Contact> pageContacts = contactService.getContactByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication){

        return "user/profile";
    }

    @GetMapping("/profile/update/{userId}")
    public String updateUserProfile(
    @PathVariable("userId") String userId,
    Model model){

        User user = userService.getUserById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));
        
        UserForm userForm = new UserForm();
        // get old data 
        userForm.setUserId(user.getUserId());
        userForm.setFirstName(user.getFirstName()); 
        userForm.setLastName(user.getLastName());
        userForm.setEmail(user.getEmail());
        userForm.setAbout(user.getAbout());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setAddress(user.getAddress());
        

        model.addAttribute("userForm", userForm);
        model.addAttribute("userId", userId);
        return "user/update_user_profile";
    }

    @PostMapping("/profile/update/{userId}")
    public String UpdateUserProfileHandler(
    @PathVariable("userId") String userId,
    @ModelAttribute("userForm") UserForm userForm,
    BindingResult bindingResult,
    Model model,
    Authentication authentication
) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("userId", userId);
        return "user/update_user_profile";
    }

    User user = userService.getUserById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    if (userForm.getEmail() == null) {
    userForm.setEmail(user.getEmail());
    }
    // Update user fields from form
    user.setFirstName(userForm.getFirstName());
    user.setLastName(userForm.getLastName());
    user.setEmail(userForm.getEmail());
    user.setAbout(userForm.getAbout());
    user.setPhoneNumber(userForm.getPhoneNumber());
    user.setAddress(userForm.getAddress());

    userService.updateUser(user);

    return "redirect:/user/profile";
}

}
