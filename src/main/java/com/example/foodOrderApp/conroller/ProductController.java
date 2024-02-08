package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.entity.Product;
import com.example.foodOrderApp.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/products";

    @Autowired
    private ProductService productService;

    @GetMapping
    public String manageProducts(Model model){
        model.addAttribute("products",productService.getAllProducts());
        return "productTable :: producttable";
    }

    @GetMapping("/add-product")
    public String productFrom(){
        return "addProduct :: productform";
    }

    @PostMapping("/addproduct")
    public String addProduct(HttpServletRequest request, Model model, @RequestParam("image")MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        Product product = Product.builder()
                .productName(request.getParameter("name"))
                .categoryName(request.getParameter("category"))
                .subCategoryName(request.getParameter("subCategory"))
                .price(Integer.parseInt(request.getParameter("price")))
                .imageURL(fileNameAndPath.toString())
                .description(request.getParameter("description"))
                .build();
        productService.addProduct(product);
        model.addAttribute("products",productService.getAllProducts());
        return "productTable :: producttable";
    }

    @DeleteMapping("/delete-product/{id}")
    public String deleteCity(@PathVariable(name = "id") String id,Model model){
       productService.deleteProductById(Long.parseLong(id));
       model.addAttribute("products",productService.getAllProducts());
       return "productTable :: producttable";
    }

    @GetMapping("/update-product/{id}")
    public String updateForm(@PathVariable(name = "id")String id, Model model){
        Product product = productService.getProductById(Long.parseLong(id));
        System.out.println(product);
        model.addAttribute("product",product);
        return "addProduct :: updateproduct";
    }

    @PatchMapping("/update/{id}")
    public String updateProduct(@ModelAttribute("product") Product product,Model model,@RequestParam("image") MultipartFile file) throws IOException{
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        Product temp = productService.getProductById(product.getId());
        temp.setProductName(product.getProductName());
        temp.setPrice(product.getPrice());
        temp.setDescription(product.getDescription());
        temp.setImageURL(fileNameAndPath.toString());
        model.addAttribute("products",productService.getAllProducts());
        return "productTable :: producttable";
    }

}
