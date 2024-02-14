package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Category;
import com.example.foodOrderApp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCatagories();

    Category addCatagory(Category category);

    void deleteCategory(Long id);

    Category findCategoryById(Long id);

    Page<Category> getPaginatedCategories(Pageable pageable);

}
