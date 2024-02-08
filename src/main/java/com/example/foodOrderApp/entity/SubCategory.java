package com.example.foodOrderApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String categoryName;
    private String subCategoryName;
    private String description;

    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
