package cz.rohlik.service.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.dto.ProductDto;
import cz.rohlik.service.stock.mapper.ProductMapper;
import cz.rohlik.service.stock.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductController(productService, productMapper))
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();

        product = new Product().setId(1L).setName("Test Product").setPrice(new BigDecimal("99.99")).setQuantityInStock(10);
        productDto = new ProductDto(1L, "Test Product", 10, new BigDecimal("99.99"));
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        given(productMapper.map(productDto)).willReturn(product);
        given(productService.createProduct(product)).willReturn(product);
        given(productMapper.map(product)).willReturn(productDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productDto.id()))
                .andExpect(jsonPath("$.name").value(productDto.name()))
                .andExpect(jsonPath("$.quantityInStock").value(productDto.quantityInStock()))
                .andExpect(jsonPath("$.price").value(productDto.price()));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        given(productMapper.map(productDto)).willReturn(product);
        given(productService.updateProduct(1L, product)).willReturn(product);
        given(productMapper.map(product)).willReturn(productDto);

        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productDto.id()))
                .andExpect(jsonPath("$.name").value(productDto.name()))
                .andExpect(jsonPath("$.quantityInStock").value(productDto.quantityInStock()))
                .andExpect(jsonPath("$.price").value(productDto.price()));
    }

    @Test
    void listProducts_shouldReturnListOfProducts() throws Exception {
        given(productService.getProducts()).willReturn(List.of(product));
        given(productMapper.map(product)).willReturn(productDto);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(productDto.id()))
                .andExpect(jsonPath("$[0].name").value(productDto.name()))
                .andExpect(jsonPath("$[0].quantityInStock").value(productDto.quantityInStock()))
                .andExpect(jsonPath("$[0].price").value(productDto.price()));
    }

    @Test
    void createProduct_shouldReturnBadRequest_whenInvalidProductDetails() throws Exception {
        var invalidProductDto = new ProductDto(null, null, -1, new BigDecimal("-99.99"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"));
    }

    @Test
    void deleteProduct_shouldReturnNotFound_whenProductNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Product not found"))
                .when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Entity not found"))
                .andExpect(jsonPath("$.description").value("Product not found"));
    }

}
