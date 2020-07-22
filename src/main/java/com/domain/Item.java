package com.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Integer priceM;
    private Integer priceL;
    private String imagePath;
    private Boolean deleted;
    private List<Topping> topping;
    private Double reviewPoint;
    private Integer genre;
}
