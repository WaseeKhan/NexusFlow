package com.nexusflow.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    private String userId;
    @NotBlank(message = "Name is required")
    @Size(min=3,message = "Name must be at least 3 characters long")
    private String name;
    
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min=3,message = "Password must be at least 3 characters long")
    private String password;
    @NotBlank(message = "About is required")
    private String about; 
    @Size(min=10, max=12, message = "Phone number must be between 10 and 12 characters")
    private String phoneNumber; 
    
    private String address;

}
