package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.service.SubCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    public String manageSubCatagory(Model model){
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subcategories",subCategories);
        return "subCategoryTable :: subcategorytable";
    }

    @GetMapping("/add-subcategory")
    public String addSubCategory(){
        return "addSubCategory :: subcategoryform";
    }


    @PostMapping("/addsubcategory")
    public String saveSubCategory(HttpServletRequest request, Model model){
        SubCategory subcategory = SubCategory.builder().categoryName(request.getParameter("categoryname")).subCategoryName(request.getParameter("name")).description(request.getParameter("description")).build();
        subCategoryService.addSubCategory(subcategory);
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subcategories",subCategories);
        return "subCategoryTable :: subcategorytable";
    }

    @DeleteMapping("/delete-subcategory/{id}")
    public String deleteSubCategory(@PathVariable(name = "id") String id, Model model){
        System.out.println(id);
        subCategoryService.deleteSubCategory(Long.parseLong(id));
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subcategories",subCategories);
        return "subCategoryTable :: subcategorytable";
    }

}
