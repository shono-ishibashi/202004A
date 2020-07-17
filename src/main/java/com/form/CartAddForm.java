package com.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CartAddForm {

    private Integer id;

    @NotNull(message = "数量を選択してください")
    private Integer quantity;

    @NotBlank(message = "サイズを選択してください")
    private String size;

    private List<Integer> toppingList;
}
