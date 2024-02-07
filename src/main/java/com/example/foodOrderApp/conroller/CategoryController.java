package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Category;
import com.example.foodOrderApp.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/add-category")
    public String addCategory(){
        return "addCategory :: categoryform";
    }

    @GetMapping
    public String manageCatagory(Model model){
        List<Category> categories = categoryService.getAllCatagories();
        model.addAttribute("catagories",categories);
        return "catagoryTable :: catagorytable";
    }

    @PostMapping("/addcategory")
    public String saveCategory(HttpServletRequest request, Model model){
        Category category = Category.builder().name(request.getParameter("name")).description(request.getParameter("description")).build();
        categoryService.addCatagory(category);
        List<Category> categories = categoryService.getAllCatagories();
        model.addAttribute("catagories",categories);
        return "catagoryTable :: catagorytable";
    }

    @DeleteMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable(name = "id") String id, Model model){
        System.out.println(id);
        categoryService.deleteCategory(Long.parseLong(id));
        List<Category> categories = categoryService.getAllCatagories();
        model.addAttribute("catagories",categories);
        return "catagoryTable :: catagorytable";
    }
}
