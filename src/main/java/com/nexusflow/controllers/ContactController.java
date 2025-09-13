package com.nexusflow.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexusflow.entities.Contact;
import com.nexusflow.entities.User;
import com.nexusflow.forms.ContactForm;
import com.nexusflow.helpers.Helper;
import com.nexusflow.helpers.Message;
import com.nexusflow.helpers.MessageType;
import com.nexusflow.services.ContactService;
import com.nexusflow.services.ImageService;
import com.nexusflow.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(ContactController.class);


    @GetMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        // contactForm.setName("Waseem");
        contactForm.setFavorite(true);
        System.out.println("addContact invoked");
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContacts( @Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        // Need to implements validation later here
        if (result.hasErrors()){
            // result.getAllErrors().forEach(error->logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
            .content("Please correct the following error(s)")
            .type(MessageType.red)
            .build()
            );
            return "user/add_contact";
        }

        //image related nprocessing


        logger.info("File Information : {} ", contactForm.getContactImage().getOriginalFilename());
       
        String filename = UUID.randomUUID().toString();

        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
        
        Contact contact = new Contact();
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setUser(user);
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        contactService.saveContact(contact);

        System.out.println("saveContacts invoked");
        System.out.println(contactForm);
        session.setAttribute("message", Message.builder()
            .content("You have successfully added new contact")
            .type(MessageType.green)
            .build()
            );
        return "redirect:/user/contacts/add";
    }

}
