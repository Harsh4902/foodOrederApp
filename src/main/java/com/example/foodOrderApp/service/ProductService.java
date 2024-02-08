package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product addProduct(Product product);

    void deleteProductById(Long id);

    Product getProductById(Long id);

}
