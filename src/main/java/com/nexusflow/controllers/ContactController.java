package com.nexusflow.controllers;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.nexusflow.entities.Contact;
import com.nexusflow.entities.User;
import com.nexusflow.forms.ContactForm;
import com.nexusflow.forms.ContactSearchForm;
import com.nexusflow.helpers.AppConstant;
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
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        // contactForm.setName("Waseem");
        contactForm.setFavorite(true);
        System.out.println("addContact invoked");
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContacts(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // Need to implements validation later here
        if (result.hasErrors()) {
            // result.getAllErrors().forEach(error->logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
                    .content("Please correct the following error(s)")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        // image related processing

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
                .build());
        return "redirect:/user/contacts/add";
    }

    @GetMapping
    public String viewContact(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getContactByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // search handler

    @GetMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {
        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContacts = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContacts = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContacts = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);

        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContacts = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);

        }
        logger.info("pageContacts {}", pageContacts);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContacts", pageContacts);
    
         model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        return "user/search";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(
        @PathVariable("contactId") String contactId,
        HttpSession session
        ) {

        contactService.deleteConatct(contactId);
        logger.info("contactId {} deleted: ", contactId);

        session.setAttribute("message", Message.builder()
                .content("Contact deleted successfully")
                .type(MessageType.green)
                .build());  

        return "redirect:/user/contacts";
       
    }

    @GetMapping("/view/{contactId}")
    public String updateContactView(
        @PathVariable String contactId,
        Model model){

        Contact conatct = contactService.getContactById(contactId);
        ContactForm contactForm = new ContactForm();

        contactForm.setName(conatct.getName());
        contactForm.setEmail(conatct.getEmail());
        contactForm.setAddress(conatct.getAddress());
        contactForm.setDescription(conatct.getDescription());
        contactForm.setPhoneNumber(conatct.getPhoneNumber());
        contactForm.setFavorite(conatct.isFavorite());
        contactForm.setWebsiteLink(conatct.getWebsiteLink());
        contactForm.setLinkedInLink(conatct.getLinkedInLink());
        contactForm.setPicture(conatct.getPicture());

        model.addAttribute("contactForm",contactForm);
        model.addAttribute("contactId", contactId);

    return "user/update_contact_view";

    }

    @PostMapping("/update/{conatctId}")
    public String updateContactHandler(
        @PathVariable String conatctId, 
        @Valid
        @ModelAttribute ContactForm contactForm,
        BindingResult bindingResult,
        Model model){

        //update conatct

        if(bindingResult.hasErrors()){

            return "user/update_contact_view";
        }

        Contact con = contactService.getContactById(conatctId);
        con.setId(conatctId);
        con.setName(contactForm.getName());
        con.setAddress(contactForm.getAddress());
        con.setEmail(contactForm.getEmail());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setWebsiteLink(contactForm.getWebsiteLink());
       

        //image update process
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            logger.info("File is not emplty");
        String fileName = UUID.randomUUID().toString();
        String imageUrl =  imageService.uploadImage(contactForm.getContactImage(), fileName);
        con.setCloudinaryImagePublicId(imageUrl);
        con.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);

        } else{
            logger.info("File is empty");
        }
       

        Contact updateCon = contactService.updateContact(con);


        logger.info("Updated Conact {}", updateCon);

        model.addAttribute("message", Message
        .builder()
        .content("Contact Updated !!")
        .type(MessageType.green)
        );

        return "redirect:/user/contacts/view/"+conatctId;

    }

}
