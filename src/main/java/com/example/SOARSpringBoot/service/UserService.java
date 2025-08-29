package com.example.SOARSpringBoot.service;

import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.exception.UserAlreadyExistsException;
import com.example.SOARSpringBoot.exception.UserNotFoundException;
import com.example.SOARSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User getUser(Long id) throws UserNotFoundException {
        try
        {
            return userRepository.findById(id).get();
        }
        catch(NoSuchElementException ex)
        {
            throw new UserNotFoundException("Cound not find user with Id"+id);
        }
    }
    private String encodePassword(User user)
    {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodedpass=encoder.encode(user.getPassword());
        return encodedpass;
    }
    public Boolean isEmailUnique(Long id,String email)
    {
        User userByEmail=userRepository.findByEmailId(email);
        if(userByEmail==null)
            return true;
        boolean isCreatingNew=(id==null);
        if(isCreatingNew)
        {
            if(userByEmail!=null) return false;
        }
        else
        {
            if(userByEmail.getId()!=id)
            {
                return false;
            }
        }
        return true;
    }
    public User updateUser(User userInForm,User logged) throws UserAlreadyExistsException {
        User user=userRepository.findById(logged.getId()).get();
        if(userInForm.getPassword()!=null) {
            if (!userInForm.getPassword().isEmpty()) {
                user.setPassword(encodePassword(userInForm));
            }
        }
        if(!userInForm.getUsername().equalsIgnoreCase(user.getUsername()))
        {
            user.setUsername(userInForm.getUsername());
        }
        if(!userInForm.getEmailId().isEmpty()) {
            if (isEmailUnique(logged.getId(), userInForm.getEmailId()))
            {
                user.setEmailId(userInForm.getEmailId());
            }
            else
            {
                throw new UserAlreadyExistsException("User with this email already exists");
            }
        }
        if(userInForm.getRole()!=null)
        {
            if(!logged.getRole().equalsIgnoreCase(userInForm.getRole()))
            {
                user.setRole(userInForm.getRole());
            }
        }
        return user;
    }
}
