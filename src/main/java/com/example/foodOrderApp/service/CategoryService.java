package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCatagories();

    Category addCatagory(Category category);

    void deleteCategory(Long id);

    Category findCategoryById(Long id);

}
