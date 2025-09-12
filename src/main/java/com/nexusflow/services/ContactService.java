package com.nexusflow.services;

import java.util.List;

import com.nexusflow.entities.Contact;

public interface ContactService {

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    List<Contact> getAllContact();

    Contact getContactById(String id);

    void deleteConatct(String id);

    List<Contact> searchContact(String name, String email, String phoneNumber);

    List<Contact> getContactByUserId(String userId);



}
