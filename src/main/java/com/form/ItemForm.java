package com.form;

import javax.validation.constraints.NotBlank;

public class ItemForm {

    @NotBlank(message = "　キーワードを入力してください")
    private String noodleName;

    public String getNoodleName() {
        return noodleName;
    }

    public void setNoodleName(String noodleName) {
        this.noodleName = noodleName;
    }
}
