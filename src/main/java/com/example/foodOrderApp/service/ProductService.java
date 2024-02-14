package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product addProduct(Product product);

    void deleteProductById(Long id);

    Product getProductById(Long id);

    Page<Product> getPaginatedProduct(Pageable pageable);

}
