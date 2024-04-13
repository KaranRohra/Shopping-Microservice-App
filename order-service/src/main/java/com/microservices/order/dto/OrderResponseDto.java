package com.microservices.order.dto;

import java.math.BigDecimal;

public record OrderResponseDto(Long id,
    String orderNumber,
    String skuCode,
    BigDecimal price,
    Integer quantity) {

}
