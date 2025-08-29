package com.example.SOARSpringBoot.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Password {

    @NotEmpty(message= "emaiId cannot be empty")
    @Email(message ="please provide valid email Id")
    private String email;
    @Size(min = 8,message = "Password must be min of 8 characters")
    @NotEmpty(message="Password cannot be empty")
    private String password;
    @Size(min = 8,message = "Password must be min of 8 characters")
    @NotEmpty(message="Password cannot be empty")
    private String retypePassword;

    public Password(String email, String password, String retypePassword) {
        this.email = email;
        this.password = password;
        this.retypePassword = retypePassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Password() {
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
}
