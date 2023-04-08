package com.tiago.ecommerce.category;

import com.tiago.ecommerce.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Product> products = new ArrayList<>();
}