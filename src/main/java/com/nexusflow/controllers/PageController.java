package com.nexusflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nexusflow.entities.User;
import com.nexusflow.forms.UserForm;
import com.nexusflow.helpers.Message;
import com.nexusflow.helpers.MessageType;
import com.nexusflow.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model){
        
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("activePage", "home");
        System.out.println("Home Page Handler");
        model.addAttribute("appName", "NexusFlow");
        model.addAttribute("appCategory", "A CRM Application");
        model.addAttribute("githubRepo","https://github.com/WaseeKhan/NexusFlow");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("activePage", "about");
        model.addAttribute("isLogin", false);
        System.out.println("About Page Handler");
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage(Model model){
        model.addAttribute("activePage", "services");
        System.out.println("Services Page Handler");
        return "services";
    }
     @GetMapping("/contact")
    public String contactPage(Model model){
        model.addAttribute("activePage", "contact");
        System.out.println("Contact Page Handler");
        return "contact";
    }

     @GetMapping("/careers")
    public String careersPage(){
        System.out.println("Careers Page Handler");
        return "careers";
    }
    @GetMapping("/apply-job")
    public String applyJobPage(){
        System.out.println("Apply Job Page Handler");
        return "apply-job";
    }

     @GetMapping("/register")
    public String registerPage(Model model){
        System.out.println("Register Page Handler");
        UserForm userForm = new UserForm();
        //setting defalut values in form
        // userForm.setName("Waseem");
        // userForm.setAbout("This is about Waseem");
        model.addAttribute("userForm", userForm);
        return "register";
    }
    @GetMapping("/login")
    public String loginPage(){
        System.out.println("Login Page Handler");
        return "login";
    }

    @PostMapping("/do-register")
    public String doRegister(@Valid @ModelAttribute UserForm userForm, BindingResult   rBindingResult, HttpSession session){
        System.out.println("Do Register Handler");
        // fetch form data
        // UserForm 
        
        // validate data 
        if(rBindingResult.hasErrors()){
            System.out.println("Error in validation: "+ rBindingResult.toString());
            return "register";
        }

        // save to db 

        User user = new User();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        // user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://res.cloudinary.com/doszwyloa/image/upload/v1758110157/defaultProfile.png");  
        
        User savedUser = userService.saveUser(user);
        System.out.println("Saved User: "+ savedUser);
        // message 
        Message message = Message.builder().content("Registration Successful !! Please Login..").type(MessageType.blue).build();
        session.setAttribute("message", message);
        //redirect
        return "redirect:/register";
    }

}
