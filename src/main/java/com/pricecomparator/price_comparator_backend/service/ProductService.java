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

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByStore(String store) {
        return productRepository.findByStore(store);
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByProductCategory(category);
    }

    public List<Product> getByProductId(String productId) {
        return productRepository.findAllByProductId(productId);
    }

    public List<Product> getByStoreAndCategory(String store, String category) {
        return productRepository.findByStoreAndProductCategory(store, category);
    }

    public List<Product> getByStoreAndProductId(String store, String productId) {
        return productRepository.findByStoreAndProductId(store, productId);
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }
    public void deleteAll(){
        productRepository.deleteAll();
    }

    public boolean exists(Product p) {
        return productRepository.existsByProductIdAndStoreAndDate(
                p.getProductId(),
                p.getStore(),
                p.getDate()
        );
    }

}