package com.example.SOARSpringBoot.controllers;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.exception.UserAlreadyExistsException;
import com.example.SOARSpringBoot.exception.UserNotFoundException;
import com.example.SOARSpringBoot.model.Password;
import com.example.SOARSpringBoot.repository.UserRepository;
import com.example.SOARSpringBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
   public List<String> roleList;

    @ModelAttribute
    public void preLoad(Model model)
    {
        roleList=new ArrayList<>();
        roleList.add("Manager");
        roleList.add("Employee Admin");
        roleList.add("Infrastructure Admin");
        roleList.add("Software Developer");
    }
    @GetMapping("")
    public String viewHomePage(Model model)
    {
       // model.addAttribute("roleList",roleList);
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model)
    {
        model.addAttribute("user",new User());
        model.addAttribute("roleList",roleList);
        return "signup-form";
    }
    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    @GetMapping("/password")
    public String changePassword(Model model)
    {
        Password password=new Password();
        model.addAttribute("password",password);
        return "change-password";
    }
    @PostMapping("/change-password")
    public String passwordProcess(final @Valid Password password,final BindingResult bindingResult,RedirectAttributes redirectAttributes,final Model model)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("password", password);
            return "change-password";
        }
        User u=userRepository.findByEmailId(password.getEmail());
        if(u==null)
        {
            redirectAttributes.addFlashAttribute("message","Invalid Email");
            return "redirect:/password";
        }
        else
        {
            if(password.getPassword().equalsIgnoreCase(password.getRetypePassword()))
            {
                BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
                String encodedPassword=encoder.encode(password.getPassword());
                u.setPassword(encodedPassword);
                userRepository.save(u);
                redirectAttributes.addFlashAttribute("message","Password Changed Successfully");
                return "redirect:/login";
            }
            else
            {
                redirectAttributes.addFlashAttribute("message","Password Doesn't Match");

                return "redirect:/password";
            }

        }
    }

    @PostMapping("/process_register")
    public String processRegistration(final @Valid User user, final BindingResult bindingResult, RedirectAttributes redirectAttributes,final Model model)
    {
        model.addAttribute("roleList",roleList);
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", user);
            return "signup-form";
        }
        User u=userRepository.findByEmailId(user.getEmailId());
        if(u!=null) {
            bindingResult.rejectValue("emailId", "user.emailId","An account already exists for this email.");
            model.addAttribute("registrationForm", user);
            return "signup-form";
        }
        else
        {
            BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
            String encodedPassword=encoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message","Registration Succes");
            return "redirect:/";
        }
    }

    @GetMapping("/emp-management")
    public String viewUserList(@AuthenticationPrincipal UserDetails loggedUser,Model model)
    {
        List<User> listUsers=userRepository.findAll();
        for(User u:listUsers)
        {
            if(u.getEmailId().equalsIgnoreCase(loggedUser.getUsername()))
            {
                listUsers.remove(u);
                break;
            }
        }
        model.addAttribute("listUsers",listUsers);
        return "users";
    }

    @GetMapping("/emp-management/profile")
    public String viewAdminDetails(@AuthenticationPrincipal UserDetails loggedUser,Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Employee Admin Profile");
            model.addAttribute("submit","/emp-management/profile/edit");
            model.addAttribute("cancel","/emp-management");
            return "account-form";
        }
        else
        {
           return  "redirect:/emp-management";
        }
    }

    @GetMapping("/emp-management/emp/edit/{id}")
    public String viewUserDetails(@PathVariable(name="id") Long id, RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User user = userService.getUser(id);
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            return "user-edit-form";
        }
        catch(UserNotFoundException ex)
        {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/emp-management";
        }
    }

    @PostMapping("/emp-management/emp/edit/{id}")
    public String editUserDetails(@PathVariable(name="id") Long id, User user,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User editable=userService.getUser(id);
            User updated=userService.updateUser(user,editable);
            userRepository.save(updated);
            return "redirect:/emp-management";
        } catch (UserNotFoundException | UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/emp-management/emp/edit/"+id;
        }
    }

    @GetMapping ("/emp-management/profile/edit")
    public String updateAdmin(@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes,Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Edit Employee Admin Profile");
            model.addAttribute("submit","/emp-management/profile/edit");
            model.addAttribute("cancel","/emp-management/profile");
            return "admin-account-edit";
        }
        else
        {
            return  "redirect:/emp-management/profile";
        }
    }

    @PostMapping("/emp-management/profile/edit")
    public String processUpdateAdmin(@AuthenticationPrincipal UserDetails loggedUser, @ModelAttribute User user,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User logged=userRepository.findByEmailId(loggedUser.getUsername());
            User updateUser=userService.updateUser(user,logged);
            userRepository.save(updateUser);
            return "redirect:/emp-management";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/emp-management/profile/edit";
        }
    }
    @GetMapping("/invt-maintenance/profile")
    public String viewAdminProfile(@AuthenticationPrincipal UserDetails loggedUser, Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Infrastructure Admin Profile");
            model.addAttribute("submit","/invt-maintenance/profile/edit");
            model.addAttribute("cancel","/invt-maintenance");
            return "account-form";
        }
        else
        {
            return  "redirect:/invt-maintenance";
        }
    }

    @GetMapping ("/invt-maintenance/profile/edit")
    public String updateInvtAdmin(@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes,Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Edit Infrastructure Admin Profile");
            model.addAttribute("submit","/invt-maintenance/profile/edit");
            model.addAttribute("cancel","/invt-maintenance/profile");
            return "admin-account-edit";
        }
        else
        {
            return  "redirect:/invt-maintenance/profile";
        }
    }
    @PostMapping("/invt-maintenance/profile/edit")
    public String processUpdateInfraAdmin(@AuthenticationPrincipal UserDetails loggedUser, @ModelAttribute User user,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User logged=userRepository.findByEmailId(loggedUser.getUsername());
            User updateUser=userService.updateUser(user,logged);
            userRepository.save(updateUser);
            return "redirect:/invt-maintenance";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/invt-maintenance/profile/edit";
        }
    }

    @GetMapping("/invt-request/profile")
    public String viewDeveloperProfile(@AuthenticationPrincipal UserDetails loggedUser, Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Software Developer Profile");
            model.addAttribute("submit","/invt-request/profile/edit");
            model.addAttribute("cancel","/invt-request");
            return "account-form";
        }
        else
        {
            return  "redirect:/invt-req";
        }
    }
    @GetMapping ("/invt-request/profile/edit")
    public String updateDeveloperDetails(@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes,Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Edit Software Developer Profile");
            model.addAttribute("submit","/invt-request/profile/edit");
            model.addAttribute("cancel","/invt-request/profile");
            return "admin-account-edit";
        }
        else
        {
            return  "redirect:/invt-request/profile";
        }
    }
    @PostMapping("/invt-request/profile/edit")
    public String processUpdateDeveloper(@AuthenticationPrincipal UserDetails loggedUser, @ModelAttribute User user,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User logged=userRepository.findByEmailId(loggedUser.getUsername());
            User updateUser=userService.updateUser(user,logged);
            userRepository.save(updateUser);
            return "redirect:/invt-request";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/invt-request/profile/edit";
        }
    }
    @GetMapping("/req-approval/profile")
    public String viewMangerProfile(@AuthenticationPrincipal UserDetails loggedUser, Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Manager Profile");
            model.addAttribute("submit","/req-approval/profile/edit");
            model.addAttribute("cancel","/req-approval");
            return "account-form";
        }
        else
        {
            return  "redirect:/req-approval";
        }
    }
    @GetMapping ("/req-approval/profile/edit")
    public String updateManagerDetails(@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes,Model model)
    {
        String email=loggedUser.getUsername();
        User user=userRepository.findByEmailId(email);
        if(user!=null) {
            model.addAttribute("roleList", roleList);
            model.addAttribute("user", user);
            model.addAttribute("title","Edit Manger Profile");
            model.addAttribute("submit","/req-approval/profile/edit");
            model.addAttribute("cancel","/req-approval/profile");
            return "admin-account-edit";
        }
        else
        {
            return  "redirect:/req-approval/profile";
        }
    }
    @PostMapping("/req-approval/profile/edit")
    public String processUpdateManager(@AuthenticationPrincipal UserDetails loggedUser, @ModelAttribute User user,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            User logged=userRepository.findByEmailId(loggedUser.getUsername());
            User updateUser=userService.updateUser(user,logged);
            userRepository.save(updateUser);
            return "redirect:/req-approval";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/req-approval/profile/edit";
        }
    }
}
