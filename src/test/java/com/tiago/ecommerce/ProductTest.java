package com.tiago.ecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiago.ecommerce.product.Product;
import com.tiago.ecommerce.utils.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpServletRequestBuilder postRequest;

    @BeforeEach
    void createRequest() throws JsonProcessingException {
        Product newProduct = new Product();
        newProduct.setName("Computer");
        newProduct.setPrice(560f);

        String requestBody = new ObjectMapper().writeValueAsString(newProduct);

        postRequest = post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
    }

    @Test
    void usersCantSaveProducts() throws Exception {
        HttpHeaders userBasicAuth = AuthUtils.createBasicAuthHeaders("user", "user");

        mvc.perform(postRequest.headers(userBasicAuth)).andExpect(status().isForbidden());
    }

    @Test
    void adminsCanSaveProducts() throws Exception {
        HttpHeaders adminBasicAuth = AuthUtils.createBasicAuthHeaders("admin", "admin");

        mvc.perform(postRequest.headers(adminBasicAuth)).andExpect(status().isOk());
    }
}
