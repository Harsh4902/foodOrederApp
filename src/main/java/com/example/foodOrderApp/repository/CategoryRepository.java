package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
