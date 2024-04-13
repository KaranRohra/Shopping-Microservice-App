package com.microservices.inventory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {
	static final String SKU_CODE = "Samsung S20";
	static final int VALID_QUANTITY = 2;
	static final int INVALID_QUANTITY = 100;

	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldReadInventoryWithValidQuantity() {
		Boolean response = RestAssured.given()
				.when()
				.queryParam("skuCode", SKU_CODE)
				.queryParam("quantity", VALID_QUANTITY)
				.get("/api/inventory/in-stock")
				.then()
				.log().all()
				.statusCode(200)
				.extract().response().as(Boolean.class);

		assertTrue(response);
	}

	@Test
	void shouldReadInventoryWithInvalidQuantity() {
		Boolean response = RestAssured.given()
				.when()
				.queryParam("skuCode", SKU_CODE)
				.queryParam("quantity", INVALID_QUANTITY)
				.get("/api/inventory/in-stock")
				.then()
				.log().all()
				.statusCode(200)
				.extract().response().as(Boolean.class);

		assertFalse(response);
	}

}
