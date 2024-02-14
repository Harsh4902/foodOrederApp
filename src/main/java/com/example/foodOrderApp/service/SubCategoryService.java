package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubCategoryService {

    List<SubCategory> getAllSubCategories();
    SubCategory addSubCategory(SubCategory subCategory);

    void deleteSubCategory(Long id);

    SubCategory findSubCategoryById(Long id);

    Page<SubCategory> getPaginatedSubCategories(Pageable pageable);

}
