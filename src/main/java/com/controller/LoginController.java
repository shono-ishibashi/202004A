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
    private String showForm(){
<<<<<<< HEAD
        System.out.println();
=======
>>>>>>> orderConfirm
        return "login";
    }

    @RequestMapping("/register-user")
    private String showInsertUser(){
        return "register_user";
    }

    @RequestMapping("/register-user/insert")
    private String insertUser(
            @Validated UserForm userForm,
            BindingResult result,
            Model model
            ){

        System.out.println(userForm);

        if(!userForm.getPassword().equals(userForm.getConfirmationPassword())){
            result.rejectValue("password", "", "パスワードが一致していません");
            result.rejectValue("confirmationPassword", "", "");

        }

        User existUser = userService.findUserByEmail(userForm.getEmail());
        if(existUser != null){
            result.rejectValue("email", "", "そのメールアドレスは既に登録されています");
        }

        if(result.hasErrors()){
            return showInsertUser();
        }

        String zipCode = userForm.getZipcode().replace("-","");

        User user = new User();
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        user.setEmail(userForm.getEmail());
        user.setAddress(userForm.getAddress());
        user.setZipcode(zipCode);
        user.setTelephone(userForm.getTelephone());

        userService.insertUser(user);
        return showForm();
    }

}
