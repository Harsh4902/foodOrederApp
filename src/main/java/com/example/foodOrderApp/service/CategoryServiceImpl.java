package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Category;
import com.example.foodOrderApp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCatagories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCatagory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }
}
