package com.microservices.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.order.dto.OrderRequestDto;
import com.microservices.order.dto.OrderResponseDto;
import com.microservices.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
    orderService.placeOrder(orderRequestDto);
    return "Order placed successfully!";
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<OrderResponseDto> getOrders() {
    return orderService.getOrders();
  }
}
