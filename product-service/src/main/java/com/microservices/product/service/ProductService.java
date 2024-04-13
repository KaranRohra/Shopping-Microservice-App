package com.microservices.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.product.dto.ProductRequestDto;
import com.microservices.product.dto.ProductResponseDto;
import com.microservices.product.model.Product;
import com.microservices.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;

  public ProductResponseDto createProduct(ProductRequestDto productRequest) {
    Product product = Product.builder()
        .name(productRequest.name())
        .description(productRequest.description())
        .price(productRequest.price())
        .build();
    productRepository.save(product);
    log.info("Product created successfully");

    return new ProductResponseDto(product.getId(), product.getName(), product.getDescription(),
        product.getPrice());
  }

  public List<ProductResponseDto> getAllProducts() {
    return productRepository.findAll().stream()
        .map(product -> new ProductResponseDto(product.getId(), product.getName(), product.getDescription(),
            product.getPrice()))
        .toList();
  }
}
