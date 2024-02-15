package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.City;
import com.example.foodOrderApp.entity.Offer;
import com.example.foodOrderApp.entity.Product;
import com.example.foodOrderApp.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/products";

    @Autowired
    private ProductService productService;

    @GetMapping
    public String manageProducts(Model model){
        return mangeProductPaged(model,1);
    }

    @GetMapping("/add-product/{page}")
    public String productFrom(Model model,@PathVariable int page){
        model.addAttribute("currentPage",page);
        return "addProduct :: productform";
    }

    @PostMapping("/addproduct/{page}")
    public String addProduct(HttpServletRequest request, Model model, @RequestParam("image")MultipartFile file,@PathVariable("page") int page) throws IOException {
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
//        model.addAttribute("products",productService.getAllProducts());
//        return "productTable :: producttable";
        return mangeProductPaged(model,page);
    }

    @DeleteMapping("/delete-product/{id}/{page}")
    public String deleteCity(@PathVariable(name = "id") String id,Model model,@PathVariable("page") int page){
       productService.deleteProductById(Long.parseLong(id));
       return mangeProductPaged(model,page);
    }

    @GetMapping("/update-product/{id}/{page}")
    public String updateForm(@PathVariable(name = "id")String id, Model model,@PathVariable("page") int page){
        Product product = productService.getProductById(Long.parseLong(id));
        System.out.println(product);
        model.addAttribute("product",product);
        model.addAttribute("currentPage",page);
        return "addProduct :: updateproduct";
    }

    @PatchMapping("/update/{id}/{page}")
    public String updateProduct(@ModelAttribute("product") Product product,Model model,@RequestParam("image") MultipartFile file,@PathVariable("page") int page) throws IOException{
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        Product temp = productService.getProductById(product.getId());
        temp.setProductName(product.getProductName());
        temp.setPrice(product.getPrice());
        temp.setDescription(product.getDescription());
        temp.setImageURL(fileNameAndPath.toString());
        return mangeProductPaged(model,page);
    }

    @GetMapping("/page/{page}")
    public String mangeProductPaged(Model model, @PathVariable("page") int page){

        int currentPage = page;
        int pageSize = 5;
        Page<Product> productPage = productService.getPaginatedProduct(PageRequest.of(currentPage-1,pageSize));
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
              .boxed()
              .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("products",productPage.getContent());
        return "productTable :: producttable";
    }
}
