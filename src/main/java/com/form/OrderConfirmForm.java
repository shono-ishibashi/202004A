package com.form;

import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.*;
import java.util.Date;

public class OrderConfirmForm {

    private Integer id;
    @NotBlank(message = "名前を入力してください")
    private String name;
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式で入力してください")
    private String mailAddress;
    @NotBlank(message = "都道府県名を入力してください")
    private String address1;
    @NotBlank(message = "市区町村名（建物名）を入力してください")
    private String address2;
    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "郵便番号はXXX-XXXXの形式で入力してください")
    private String zipCode;
    @NotBlank(message = "電話番号を入力してください")
    @Pattern(regexp = "^[0-9]{2,4}-[0-9]{2,4}-[0-9]{3,4}$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください")
    private String telephone;
    @NotNull(message = "配達日を選択してください")
    private String orderDate;
    @NotEmpty(message = "配達時間を選択してください")
    private String orderTime;
    @NotNull(message = "お支払い方法を選択してください")
    private Integer paymentMethod;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }



    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
