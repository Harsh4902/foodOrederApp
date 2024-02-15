package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Override
    public SubCategory findSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow();
    }

    @Override
    public Page<SubCategory> getPaginatedSubCategories(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategory> list;

        if(subCategories.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, subCategories.size());
            list = subCategories.subList(startItem, toIndex);
        }

        Page<SubCategory> subCategoryPage = new PageImpl<>(list, PageRequest.of(currentPage,pageSize),subCategories.size());
        return subCategoryPage;
    }
}
