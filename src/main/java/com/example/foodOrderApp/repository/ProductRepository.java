package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
