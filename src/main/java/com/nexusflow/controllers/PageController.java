package com.nexusflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nexusflow.entities.User;
import com.nexusflow.forms.UserForm;
import com.nexusflow.helpers.Message;
import com.nexusflow.helpers.MessageType;
import com.nexusflow.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");
        model.addAttribute("appName", "NexusFlow");
        model.addAttribute("appCategory", "A CRM Application");
        model.addAttribute("githubRepo","https://github.com/WaseeKhan/NexusFlow");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About Page Handler");
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage(){
        System.out.println("Services Page Handler");
        return "services";
    }
     @GetMapping("/contact")
    public String contactPage(){
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
        System.out.println("Register Page Handler");
        return "login";
    }

    @PostMapping("/do-register")
    public String doRegister(@ModelAttribute UserForm userForm, HttpSession session){
        System.out.println("Do Register Handler");
        // fetch form data
        // UserForm 
        System.out.println(userForm);
        // validate data 
        // save to db 
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Flcwd_logo.45da3818.png&w=1080&q=75")
        // .build();
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Flcwd_logo.45da3818.png&w=1080&q=75");  
        
        User savedUser = userService.saveUser(user);
        System.out.println("Saved User: "+ savedUser);
        // message 
        Message message = Message.builder().content("Registration Successful !! Please Login..").type(MessageType.blue).build();
        session.setAttribute("message", message);
        //redirect
        return "redirect:/register";
    }

}
