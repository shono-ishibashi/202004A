package com.form;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.*;
import java.util.Date;

@Data
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
    @NotBlank(message = "配達日を選択してください")
    private String orderDate;
    @NotEmpty(message = "配達時間を選択してください")
    private String orderTime;
    @NotNull(message = "お支払い方法を選択してください")
    private Integer paymentMethod;

    private String couponCode;
}
