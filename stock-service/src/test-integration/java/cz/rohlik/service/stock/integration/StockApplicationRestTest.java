package cz.rohlik.service.stock.integration;

import cz.rohlik.service.stock.domain.OrderStatus;
import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderItemRequest;
import cz.rohlik.service.stock.dto.ProductDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Disabled("Manual test")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StockApplicationRestTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void createProduct_shouldCreateProductSuccessfully() {
        createProduct();
    }

    @Test
    @Order(2)
    void updateProduct_shouldUpdateProductDetails() {
        var productId = createProduct();
        var updatedProductDto = new ProductDto(null, "Updated Product", 30, new BigDecimal("300.0"));

        given()
                .contentType(ContentType.JSON)
                .body(updatedProductDto)
                .when()
                .put("/products/{id}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated Product"))
                .body("quantityInStock", equalTo(30))
                .body("price", equalTo(300.0f));
    }

    @Test
    @Order(3)
    void getProducts_shouldReturnTwoProducts() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("[0].name", equalTo("Test Product"))
                .body("[0].quantityInStock", equalTo(10))
                .body("[0].price", equalTo(100.0f))
                .body("[1].name", equalTo("Updated Product"))
                .body("[1].quantityInStock", equalTo(30))
                .body("[1].price", equalTo(300.0f));
    }

    @Test
    @Order(4)
    void deleteProduct_shouldDeleteProductSuccessfully() {
        var productId = createProduct();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/products/{id}", productId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(5)
    void createOrder_shouldCreateOrderSuccessfully() {
        createOrder();
    }

    @Test
    @Order(6)
    void setOrderPaid_shouldUpdateOrderStatusToPaid() {
        var orderId = createOrder();

        given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/orders/{id}/pay", orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(OrderStatus.PAID.name()));
    }

    @Test
    @Order(7)
    void cancelOrder_shouldUpdateOrderStatusToCanceled() {
        var orderId = createOrder();

        given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/orders/{id}/cancel", orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(OrderStatus.CANCELED.name()));
    }

    private Long createProduct() {
        var productDto = new ProductDto(null, "Test Product", 10, new BigDecimal("100.0"));

        Integer productId =
                given()
                        .contentType(ContentType.JSON)
                        .body(productDto)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .path("id");
        return productId.longValue();
    }

    private Long createOrder() {
        var productId = createProduct();
        var orderItemRequest = new OrderItemRequest(productId, 1);
        var createOrderRequest = new CreateOrderRequest(List.of(orderItemRequest));

        Integer orderId =
                given()
                        .contentType(ContentType.JSON)
                        .body(createOrderRequest)
                        .when()
                        .post("/orders")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .path("id");
        return orderId.longValue();
    }

}
