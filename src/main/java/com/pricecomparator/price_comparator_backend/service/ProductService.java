package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.dto.PricePointDto;
import com.pricecomparator.price_comparator_backend.dto.ValuePerUnitDto;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.text.Normalizer.normalize;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountApplierService discountApplierService;

    private BigDecimal normalizeQuantity(double quantity, String unit) {
        return switch (unit.toLowerCase()) {
            case "g"     -> BigDecimal.valueOf(quantity).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); // g → kg
            case "ml"    -> BigDecimal.valueOf(quantity).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP); // ml → l
            case "kg", "l", "buc" -> BigDecimal.valueOf(quantity); // already normalized
            default      -> BigDecimal.valueOf(quantity); // fallback, no conversion
        };
    }


    public ProductService(
            ProductRepository productRepository,
            DiscountApplierService discountApplierService
    ) {
        this.productRepository = productRepository;
        this.discountApplierService = discountApplierService;
    }

    private String normalize(String input) {
        return input == null ? "" : input.trim().toLowerCase();
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

    @Autowired
    private PricePerUnitService pricePerUnitService;

    public List<ValuePerUnitDto> getRecommendedProducts(String productName, String brand) {
        List<Product> products = productRepository.findAll();

        return products.stream()
                // If brand is provided → do exact match
                .filter(p -> normalize(p.getProductName()).equalsIgnoreCase(normalize(productName)))
                .filter(p -> brand == null || normalize(p.getBrand()).equalsIgnoreCase(normalize(brand)))

                // If brand is not provided → include similar product names
                .filter(p -> brand != null || normalize(p.getProductName()).contains(normalize(productName)))

                .filter(p -> p.getPackageQuantity() > 0)
                .map(p -> {
                    BigDecimal discountedPrice = discountApplierService.getDiscountedPrice(p);
                    BigDecimal pricePerUnit = pricePerUnitService.computePricePerUnit(
                            discountedPrice,
                            p.getPackageQuantity(),
                            p.getPackageUnit()
                    );
                    String baseUnit = pricePerUnitService.getUnitLabel(p.getPackageUnit());

                    return new ValuePerUnitDto(
                            p.getProductId(),
                            p.getProductName(),
                            p.getBrand(),
                            p.getStore(),
                            p.getProductCategory(),
                            discountedPrice, // ✅ show discounted price
                            p.getPackageQuantity(),
                            p.getPackageUnit(),
                            pricePerUnit,
                            baseUnit
                    );
                })
                .sorted(Comparator.comparing(ValuePerUnitDto::pricePerUnit))
                .limit(10)
                .toList();
    }



}