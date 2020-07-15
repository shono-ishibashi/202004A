package com.controller;

import com.domain.User;
import com.form.UserForm;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public UserForm userInsertSetUp(){
        return new UserForm();
    }

    @RequestMapping("/login-form")
    private String showForm(String error){
        System.out.println(error);
        return "login";
    }

    @RequestMapping("/register-user")
    private String showInsertUser(){
        return "register_user";
    }

    @RequestMapping("/register-user/insert")
    private String insertUser(
            @Validated UserForm userForm,
            BindingResult retult
            ){

        if(retult.hasErrors()){
            return showInsertUser();
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        user.setEmail(userForm.getEmail());
        user.setAddress(userForm.getAddress());
        user.setZipcode(userForm.getZipcode());
        user.setTelephone(userForm.getTelephone());

        userService.insertUser(user);
        return "redirect:login";
    }

}
