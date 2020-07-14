package com.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserForm {
    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "メッセージを入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    private String password;

    @NotBlank(message = "郵便番号を入力してください")
    private String zipcode;

    @NotBlank(message = "住所を入力してください")
    private String address;

    @NotBlank(message = "電話番号を入力してください")
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
}
