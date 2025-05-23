package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.dto.PricePointDto;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountApplierService discountApplierService;

    public ProductService(
            ProductRepository productRepository,
            DiscountApplierService discountApplierService
    ) {
        this.productRepository = productRepository;
        this.discountApplierService = discountApplierService;
    }

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

    public List<PricePointDto> getPriceHistory(
            String productId,
            String store,
            String brand,
            String category,
            LocalDate from,
            LocalDate to
    ) {
        List<Product> products = productRepository.findAllByProductId(productId);

        return products.stream()
                .filter(p -> store == null || p.getStore().equalsIgnoreCase(store))
                .filter(p -> brand == null || p.getBrand().equalsIgnoreCase(brand))
                .filter(p -> category == null || p.getProductCategory().equalsIgnoreCase(category))
                .filter(p -> from == null || !p.getDate().isBefore(from))
                .filter(p -> to == null || !p.getDate().isAfter(to))
                .map(p -> {
                    BigDecimal discountedPrice = discountApplierService.getDiscountedPrice(p);
                    return new PricePointDto(p.getDate(), p.getStore(), discountedPrice);
                })
                .sorted(Comparator.comparing(PricePointDto::date))
                .toList();
    }

}