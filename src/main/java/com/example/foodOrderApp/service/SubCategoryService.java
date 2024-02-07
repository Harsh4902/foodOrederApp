package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.SubCategory;

import java.util.List;

public interface SubCategoryService {

    List<SubCategory> getAllSubCategories();
    SubCategory addSubCategory(SubCategory subCategory);

    void deleteSubCategory(Long id);

}
