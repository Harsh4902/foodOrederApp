package com.example.foodOrderApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String offerName;
    private String categoryName;
    private String subCategoryName;
    private String restaurantName;
    private int discount;
    private String description;
    private String startDate;
    private String endDate;

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", offerName='" + offerName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
