package com.nexusflow.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nexusflow.entities.Contact;
import com.nexusflow.entities.User;
import com.nexusflow.helpers.ResourceNotFoundException;
import com.nexusflow.repositories.ContactRepository;
import com.nexusflow.services.ContactService;

@Service
public class ContactServiceImpl  implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {
        String contactId =  UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }

    @Override
    public List<Contact> getAllContact() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException( "Contact not found with given id" +id));
    }

    @Override
    public void deleteConatct(String id) {
    var contact = contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException( "Contact not found with given id" +id));
    contactRepository.delete(contact);
    }

    @Override
    public List<Contact> searchContact(String name, String email, String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchContact'");
    }

    @Override
    public List<Contact> getContactByUserId(String userId) {
       return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getContactByUser(User user, int page, int size, String sortBy, String direction ) {
        Sort  sort =  direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
        PageRequest pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUser(user, pageable);
    }

}
