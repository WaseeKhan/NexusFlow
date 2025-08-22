package com.nexusflow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

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

     @GetMapping("/register")
    public String registerPage(){
        System.out.println("Register Page Handler");
        return "register";
    }
     @GetMapping("/login")
    public String loginPage(){
        System.out.println("Register Page Handler");
        return "login";
    }

}
