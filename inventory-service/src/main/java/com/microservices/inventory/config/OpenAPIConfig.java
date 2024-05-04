package com.microservices.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
  @Bean
  public OpenAPI inventoryOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Inventory Service API").version("1.0").description("Documentation Inventory API v1.0"));
  }
}
