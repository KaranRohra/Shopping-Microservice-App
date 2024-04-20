package com.microservices.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;

import com.microservices.order.stubs.InventoryClientStub;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0) // Random port for WireMock
class OrderServiceApplicationTests {

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
	void shouldPlaceOrder() {
		String placeOrderJson = """
				{
					"skuCode": "ABC123",
					"price": 100.0,
					"quantity": 1
				}
				""";
		InventoryClientStub.stubInventoryCall("ABC123", 1);

		String expectedResponseBody = "Order placed successfully!";

		var responseBodyString = RestAssured.given()
				.body(placeOrderJson)
				.contentType("application/json")
				.post("/api/order")
				.then()
				.statusCode(201)
				.extract()
				.path("message");
		assertEquals(expectedResponseBody, responseBodyString);
	}

	@Test
	void shouldReturnAllOrders() {
		RestAssured.given()
				.get("/api/order")
				.then()
				.statusCode(200)
				.body("size()", Matchers.is(1));
	}
}
