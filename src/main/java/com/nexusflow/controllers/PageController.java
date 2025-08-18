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

}
