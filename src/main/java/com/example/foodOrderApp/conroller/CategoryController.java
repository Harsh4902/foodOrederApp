package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Category;
import com.example.foodOrderApp.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/add-category/{page}")
    public String addCategory(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        return "addCategory :: categoryform";
    }

    @GetMapping
    public String manageCatagory(Model model){
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model, 1);
    }

    @PostMapping("/addcategory/{page}")
    public String saveCategory(HttpServletRequest request, Model model,@PathVariable("page") int page){
        Category category = Category.builder().name(request.getParameter("name")).description(request.getParameter("description")).build();
        categoryService.addCatagory(category);
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model, page);
    }

    @DeleteMapping("/delete-category/{id}/{page}")
    public String deleteCategory(@PathVariable(name = "id") String id, Model model,@PathVariable("page") int page){
        System.out.println(id);
        categoryService.deleteCategory(Long.parseLong(id));
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model,page);
    }

    @GetMapping("/update-category/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        Category category = categoryService.findCategoryById(Long.parseLong(id));
        System.out.println(category);
        model.addAttribute("category",category);
        model.addAttribute("currentPage",page);
        return "addCategory :: updatecategory";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateCategory(@ModelAttribute("category") Category category,Model model,@PathVariable("page") int page){
        Category temp = categoryService.findCategoryById(category.getId());
        temp.setName(category.getName());
        temp.setDescription(category.getDescription());
        categoryService.addCatagory(temp);
//        List<Category> categories = categoryService.getAllCatagories();
//        model.addAttribute("catagories",categories);
//        return "catagoryTable :: catagorytable";
        return mangeCategoryPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeCategoryPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Category> categoryPage = categoryService.getPaginatedCategories(PageRequest.of(currentPage-1,pageSize));
        int totalPages = categoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("catagories",categoryPage.getContent());
        return "catagoryTable :: catagorytable";
    }
}
