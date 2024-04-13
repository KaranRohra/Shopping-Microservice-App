package com.microservices.product;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
					"name": "Iphone 15",
					"description": "Iphone 15 is a Smartphone build by Apple",
					"price": 1000
				}
				""";
		RestAssured.given()
				.contentType("Application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Iphone 15"))
				.body("description", Matchers.equalTo("Iphone 15 is a Smartphone build by Apple"))
				.body("price", Matchers.equalTo(1000));
	}

	@Test
	void shouldGetAllProducts() {
		RestAssured.given()
				.when()
				.get("/api/product")
				.then()
				.statusCode(200)
				.body("size()", Matchers.equalTo(1));
	}
}
