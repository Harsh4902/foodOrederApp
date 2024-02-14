package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.SubCategory;
import com.example.foodOrderApp.service.SubCategoryService;
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
@RequestMapping("/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    public String manageSubCatagory(Model model){
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, 1);
    }

    @GetMapping("/add-subcategory/{page}")
    public String addSubCategory(Model model,@PathVariable("page") int page){
        model.addAttribute("currentPage",page);
        return "addSubCategory :: subcategoryform";
    }


    @PostMapping("/addsubcategory/{page}")
    public String saveSubCategory(HttpServletRequest request, Model model,@PathVariable("page") int page){
        SubCategory subcategory = SubCategory.builder().categoryName(request.getParameter("categoryname")).subCategoryName(request.getParameter("name")).description(request.getParameter("description")).build();
        subCategoryService.addSubCategory(subcategory);
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @DeleteMapping("/delete-subcategory/{id}/{page}")
    public String deleteSubCategory(@PathVariable(name = "id") String id, Model model,@PathVariable("page") int page){
        System.out.println(id);
        subCategoryService.deleteSubCategory(Long.parseLong(id));
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @GetMapping("/update-subcategory/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        SubCategory subcategory = subCategoryService.findSubCategoryById(Long.parseLong(id));
        System.out.println(subcategory);
        model.addAttribute("subcategory",subcategory);
        model.addAttribute("currentPage",page);
        return "addSubCategory :: updatesubcategory";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateCategory(@ModelAttribute("subcategory") SubCategory subcategory,Model model,@PathVariable("page") int page){
        System.out.println(subcategory);
        SubCategory temp = subCategoryService.findSubCategoryById(subcategory.getId());
        temp.setSubCategoryName(subcategory.getSubCategoryName());
        temp.setDescription(subcategory.getDescription());
        subCategoryService.addSubCategory(temp);
//        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
//        model.addAttribute("subcategories",subCategories);
//        return "subCategoryTable :: subcategorytable";
        return mangeSubCategoryPaged(model, page);
    }

    @GetMapping("/page/{page}")
    public String mangeSubCategoryPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<SubCategory> subCategoryPage = subCategoryService.getPaginatedSubCategories(PageRequest.of(currentPage-1,pageSize));
        int totalPages = subCategoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", subCategoryPage.getTotalElements());
        model.addAttribute("subcategories",subCategoryPage.getContent());
        return "subCategoryTable :: subcategorytable";
    }

}
