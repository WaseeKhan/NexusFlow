package com.nexusflow.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nexusflow.entities.Contact;
import com.nexusflow.entities.User;

public interface ContactService {

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    List<Contact> getAllContact();

    Contact getContactById(String id);

    void deleteConatct(String id);

    Page<Contact> searchByName(String nameKeyword , int size, int page, String sortBy, String order,User user);
    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order ,User user);
    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,User user);

    List<Contact> getContactByUserId(String userId);

    Page<Contact> getContactByUser(User user, int page , int size, String sortField, String sortDirection);



}
