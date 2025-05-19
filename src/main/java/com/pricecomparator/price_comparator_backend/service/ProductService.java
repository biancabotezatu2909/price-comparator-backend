package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void saveAll(List<Product> products){
        productRepository.saveAll(products);
    }
}
