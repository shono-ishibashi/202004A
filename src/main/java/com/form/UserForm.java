package com.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UserForm {
    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "Ｅメールアドレスを入力してください")
    @Email(message = "Ｅメールアドレスを入力してください。")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min=8, max=16, message = "パスワードは8文字以上16文字以下で入力してください")
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmationPassword;

    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(message = "郵便番号はXXX-XXXの形式で入力してください", regexp = "[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]")
    private String zipcode;

    @NotBlank(message = "住所を入力してください")
    private String address;

    @NotBlank(message = "電話番号を入力してください")
    @Pattern(message = "電話番号はXXX-XXX-XXXXの形式で入力してください", regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}")
    private String telephone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }
    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmationPassword='" + confirmationPassword + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
