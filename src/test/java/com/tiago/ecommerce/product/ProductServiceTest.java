package com.tiago.ecommerce.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    private ProductService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository);
    }

    @Test
    void canSaveProductToDatabase() {
        // given
        Product product = new Product();
        product.setName("PC");
        product.setPrice(500f);

        // when
        underTest.save(product);

        // then
        verify(productRepository).save(product);
    }

    @Test
    void willSend404StatusIfProductDoesNotExist() {
        // given
        final String PRODUCT_NOT_FOUND = "Product not found";
        UUID productId = UUID.randomUUID();
        ProductDto productDto = new ProductDto("PC", 500f, null, null);
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        // when
        List<ResponseEntity<Object>> responseEntities = new ArrayList<>();
        responseEntities.add(underTest.delete(productId));
        responseEntities.add(underTest.getById(productId));
        responseEntities.add(underTest.update(productId, productDto));

        // then
        ResponseEntity<String> expectedResponseEntity = ResponseEntity.status(404).body(PRODUCT_NOT_FOUND);

        for (ResponseEntity<Object> responseEntity : responseEntities) {
            assertThat(responseEntity).isEqualTo(expectedResponseEntity);
        }

        verify(productRepository, never()).delete(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void canDeleteProduct() {
        // given
        UUID productId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);
        product.setName("PC");
        product.setPrice(500f);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        // when
        ResponseEntity<Object> responseEntity = underTest.delete(productId);

        // then
        verify(productRepository).delete(product);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(ResponseEntity.ok().build().getStatusCode());
    }

    @Test
    void canGetProductById() {
        // given
        UUID productId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);
        product.setName("PC");
        product.setPrice(500f);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        // when
        ResponseEntity<Object> responseEntity = underTest.getById(productId);

        // then
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok(product));
    }

    @Test
    void canGetAllProducts() {
        // when
        underTest.getAll();

        // then
        verify(productRepository).findAll();
    }

    @Test
    void canUpdateProduct() {
        // given
        UUID productId = UUID.randomUUID();
        ProductDto productDto = new ProductDto("Updated PC", 500f, null, null);

        Product product = new Product();
        product.setId(productId);
        product.setName("PC");
        product.setPrice(500f);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(productRepository.save(product))
                .willReturn(product);

        // when
        ResponseEntity<Object> responseEntity = underTest.update(productId, productDto);

        // then
        BeanUtils.copyProperties(productDto, product);
        verify(productRepository).save(product);
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok(product));
    }

    @Test
    void canGetAllProductByCategory() {
        // given
        String categoryName = "laptops";

        // when
        underTest.getAllByCategory(categoryName);

        // then
        verify(productRepository).findByCategories_NameIgnoreCase(categoryName);
    }
}