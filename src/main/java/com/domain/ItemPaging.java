package com.domain;

import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class ItemPaging {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name = "price_m")
    private Integer priceM;

    @Column(name="price_l")
    private Integer priceL;

    @Column(name="image_path")
    private String imagePath;

    @Column(name="deleted")
    private Boolean deleted;

    @Column(name="genre")
    private Integer genre;
}
