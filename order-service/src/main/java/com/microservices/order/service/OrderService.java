package com.microservices.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microservices.order.client.InventoryClient;
import com.microservices.order.dto.OrderRequestDto;
import com.microservices.order.dto.OrderResponseDto;
import com.microservices.order.model.Order;
import com.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;

  public void placeOrder(OrderRequestDto orderRequestDto) {
    boolean isInStock = inventoryClient.isInStock(orderRequestDto.skuCode(), orderRequestDto.quantity());
    if (!isInStock) {
      throw new RuntimeException("Product is out of stock");
    }

    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    order.setSkuCode(orderRequestDto.skuCode());
    order.setPrice(orderRequestDto.price());
    order.setQuantity(orderRequestDto.quantity());
    orderRepository.save(order);
  }

  public List<OrderResponseDto> getOrders() {
    return orderRepository.findAll().stream().map(order -> new OrderResponseDto(order.getId(), order.getOrderNumber(),
        order.getSkuCode(), order.getPrice(), order.getQuantity())).toList();
  }
}
