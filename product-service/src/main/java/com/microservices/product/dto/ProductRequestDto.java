package com.microservices.product.dto;

import java.math.BigDecimal;

public record ProductRequestDto(String id, String name, String description, BigDecimal price) {
}