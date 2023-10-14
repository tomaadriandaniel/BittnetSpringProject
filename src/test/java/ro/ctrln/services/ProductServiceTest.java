package ro.ctrln.services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.enums.Currencies;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;
    
    @TestConfiguration
    static class ProductServiceTestContextConfiguration{

        @MockBean
        private ProductMapper productMapper;

        @MockBean
        private ProductRepository productRepository;

        @Bean
        public ProductService productService(){
            return new ProductService(productRepository, productMapper);
        }

    }

    @Test
    public void addProduct() {
        Product productOne = new Product();
        productOne.setDescription("A beautiful product");
        productOne.setCurrency(Currencies.USD);
        productOne.setPrice(100);
        productOne.setStock(2);
        productOne.setValid(true);
        productOne.setCode("ProductCodeOne");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("A beautiful product");
        productDTO.setCurrency(Currencies.USD);
        productDTO.setPrice(100);
        productDTO.setStock(2);
        productDTO.setValid(true);
        productDTO.setCode("ProductCodeOne");

        Mockito.when(productMapper.toEntity(Mockito.any())).thenReturn(productOne);

        productService.addProduct(productDTO, 1L);

        Mockito.verify(productMapper).toEntity(productDTO);
        Mockito.verify(productRepository).save(productOne);

    }


    @Test
    public void getProduct_whenProductIsNotInDB_shouldThrowAnException(){
        try {
            productService.getProduct("cod");
        } catch (InvalidProductCodeException e) {
            assert true;
            return;
        }

        assert false;
    }


    @Test
    public void _getProduct_whenProductIsInDB_shouldReturnIt() throws InvalidProductCodeException {
        Product product = new Product();
        product.setCode("aCode");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aCode");

        when(productRepository.findByCode(Mockito.any())).thenReturn(Optional.of(product));
        when(productMapper.toDTO(Mockito.any())).thenReturn(productDTO);

        ProductDTO returnedProductDTO = productService.getProduct("aCode");
        assertThat(returnedProductDTO.getCode()).isEqualTo("aCode");

        verify(productRepository).findByCode("aCode");
        verify(productMapper).toDTO(product);

    }


    @Test
    public void getProducts() {
        List<Product> products = new ArrayList<>();

        Product productOne = new Product();
        productOne.setCode("aCode");
        Product productTwo = new Product();
        productTwo.setCode("bCode");

        products.add(productOne);
        products.add(productTwo);

        ProductDTO productDTOOne = new ProductDTO();
        productDTOOne.setCode("aCode");
        ProductDTO productDTOTwo = new ProductDTO();
        productDTOTwo.setCode("bCode");


        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDTO(productOne)).thenReturn(productDTOOne);
        when(productMapper.toDTO(productTwo)).thenReturn(productDTOTwo);


        List<ProductDTO> productList = productService.getProducts();
        assertThat(productList).hasSize(2);
        assertThat(productList).containsOnly(productDTOOne, productDTOTwo);

        verify(productRepository).findAll();
        verify(productMapper).toDTO(productOne);
        verify(productMapper).toDTO(productTwo);

    }


    @Test
    public void updateProduct_whenProductCodeIsNull_shouldThrowAnException(){
        ProductDTO productDTO = new ProductDTO();
        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(() ->
                        productService.updateProduct(productDTO,1L), InvalidProductCodeException.class);
        assertThat(invalidProductCodeException).isNotNull();


    }


    @Test
    public void updateProduct_whenProductCodeIsInvalid_shouldThrowAnException(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("code");
        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(() ->
                        productService.updateProduct(productDTO,1L), InvalidProductCodeException.class);
        assertThat(invalidProductCodeException).isNotNull();


    }


    @Test
    public void updateProduct_whenProductCodeIsValid_shouldUpdateTheProduct() throws InvalidProductCodeException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("a new code");
        productDTO.setDescription("a new description");

        Product product = new Product();
        product.setCode("old code");
        product.setDescription("old description");

        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        productService.updateProduct(productDTO, 1L);

        verify(productRepository).findByCode(productDTO.getCode());
        verify(productRepository).save(product);

    }


    @Test
    public void deleteProduct_whenProductDTOIsNull_shouldThrowAnException(){
        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(()-> productService.deleteProduct(null, 1L), InvalidProductCodeException.class);
        assertThat(invalidProductCodeException).isNotNull();
    }


    @Test
    public void deleteProduct_whenProductCodeIsValid_shouldDeleteTheProduct() throws InvalidProductCodeException {
        Product product = new Product();
        product.setCode("aCode");

        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        productService.deleteProduct("aCode",1L);

        verify(productRepository).findByCode("aCode");
        verify(productRepository).delete(product);
    }

}



