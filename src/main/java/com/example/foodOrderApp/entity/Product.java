package com.example.foodOrderApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String categoryName;
    private String subCategoryName;
    private String productName;
    private String imageURL;
    private int price;
    private String description;
}
