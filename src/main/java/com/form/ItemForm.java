package com.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemForm {
    private String noodleName;
    private Integer sortNumber;
}
