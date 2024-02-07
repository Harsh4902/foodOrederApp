package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public SubCategory addSubCategory(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public void deleteSubCategory(Long id) {
        subCategoryRepository.deleteById(id);
    }
}
