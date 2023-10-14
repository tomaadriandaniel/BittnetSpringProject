package ro.ctrln.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Address;
import ro.ctrln.entities.Product;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Currencies;
import ro.ctrln.enums.Roles;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.repositories.UserRepository;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    public static final String LOCALHOST = "http://localhost:";

    @LocalServerPort
    private int port;


    @Autowired
    private ProductController productController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void contextLoads(){
        assertThat(productController).isNotNull();
    }

    @Test
    public void addProduct_whenUserIsAdmin_shouldStoreTheProduct(){
        productRepository.deleteAll();
        User user = getUserWithRole(Roles.ADMIN);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aCode");
        productDTO.setDescription("aDescription");
        productDTO.setCurrency(Currencies.RON);
        productDTO.setPrice(100);
        productDTO.setStock(10);
        productDTO.setValid(true);

        testRestTemplate.postForEntity(LOCALHOST+port+"/product/"+user.getId(),productDTO,Void.class);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(1);

        Product product = products.iterator().next();

        assertThat(product.getCode()).isEqualTo("aCode");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getCurrency()).isEqualTo(Currencies.RON);

    }


    @Test
    public void addProduct_whenUserIsNotAdmin_shouldThrowAnException(){

        User user = getUserWithRole(Roles.CLIENT);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aCode");
        productDTO.setDescription("aDescription");
        productDTO.setCurrency(Currencies.RON);
        productDTO.setPrice(100);
        productDTO.setStock(10);
        productDTO.setValid(true);

        ResponseEntity<String> response  = testRestTemplate.postForEntity(LOCALHOST+port+"/product/"+user.getId(),productDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User not allowed to add products!");

    }


    @Test
    public void addProduct_whenUserIsNotInDB_shouldThrowAnException(){

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aCode");
        productDTO.setDescription("aDescription");
        productDTO.setCurrency(Currencies.RON);
        productDTO.setPrice(100);
        productDTO.setStock(10);
        productDTO.setValid(true);

        ResponseEntity<String> response = testRestTemplate.postForEntity(LOCALHOST+port+"/product/5",productDTO, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Customer ID is not valid!");

    }


    @Test
    public void getProductByCode_whenCodeIsInDB_shouldReturnTheProduct(){
        Product product = getProduct("aWonderfulCode");
        saveProductToDB(Arrays.asList(product));
        ProductDTO productDTO = testRestTemplate.getForObject(LOCALHOST + port + "/product/" + product.getCode(), ProductDTO.class);

        assertThat(productDTO.getCode()).isEqualTo(product.getCode());

    }


    @Test
    public void getProductByCode_whenCodeIsNotInDB_shouldThrowAnException(){
        String response = testRestTemplate.getForObject(LOCALHOST + port + "/product/1234", String.class);

        assertThat(response).isEqualTo("Product code is not valid!");

    }


    @Test
    public void getProducts(){
        productRepository.deleteAll();
        Product productOne = getProduct("code1");
        Product productTwo = getProduct("code2");
        saveProductToDB(Arrays.asList(productOne,productTwo));

        ResponseEntity<List<ProductDTO>> response = testRestTemplate.exchange(LOCALHOST + port + "/product/",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductDTO>>(){});

        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getCode()).isEqualTo("code1");
        assertThat(response.getBody().get(1).getCode()).isEqualTo("code2");

    }


    @Test
    public void updateProduct_whenUserIsEditor_shouldUpdateProduct(){
        Product product = getProduct("aCode");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setPrice(200);
        productDTO.setValid(false);
        productDTO.setStock(10);
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setDescription("a description");

        User user = getUserWithRole(Roles.EDITOR);
        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);
        Optional<Product> updatedProduct = productRepository.findByCode("aCode");

        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(productDTO.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(productDTO.getCurrency());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(productDTO.getDescription());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(productDTO.getPrice());
        assertThat(updatedProduct.get().isValid()).isEqualTo(productDTO.isValid());
    }


    @Test
    public void updateProduct_whenUserIsAdmin_shouldUpdateProduct(){
        Product product = getProduct("aCodeAdmin");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setPrice(200);
        productDTO.setValid(false);
        productDTO.setStock(10);
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setDescription("a description admin");

        User user = getUserWithRole(Roles.ADMIN);
        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);
        Optional<Product> updatedProduct = productRepository.findByCode("aCodeAdmin");

        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(productDTO.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(productDTO.getCurrency());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(productDTO.getDescription());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(productDTO.getPrice());
        assertThat(updatedProduct.get().isValid()).isEqualTo(productDTO.isValid());
    }


    @Test
    public void updateProduct_whenUserIsClient_shouldNotUpdateProduct(){
        Product product = getProduct("aCodeClient");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setPrice(200);
        productDTO.setValid(false);
        productDTO.setStock(10);
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setDescription("a description client");

        User user = getUserWithRole(Roles.CLIENT);
        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);
        Optional<Product> updatedProduct = productRepository.findByCode("aCodeClient");

        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(product.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(product.getCurrency());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(product.getDescription());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(product.getPrice());
        assertThat(updatedProduct.get().isValid()).isEqualTo(product.isValid());
    }


    @Test
    public void deleteProduct_whenUserIsClient_shouldNotDeleteProduct(){
        Product product = getProduct("aProductClientDelete");
        productRepository.save(product);

        testRestTemplate.delete(LOCALHOST+port+"/product/"+product.getCode()+"/2");
        assertThat(productRepository.findByCode(product.getCode())).isPresent();

    }


    @Test
    public void deleteProduct_whenUserIsAdmin_shouldDeleteProduct(){
        productRepository.deleteAll();
        Product product = getProduct("aProductAdminDelete");
        productRepository.save(product);
        User user = getUserWithRole(Roles.ADMIN);

        testRestTemplate.delete(LOCALHOST+port+"/product/"+product.getCode()+"/"+user.getId());
        assertThat(productRepository.findByCode(product.getCode())).isNotPresent();

    }






    private void saveProductToDB(List<Product> products) {
        productRepository.saveAll(products);

    }



    private Product getProduct(String code) {
        Product product = new Product();
        product.setCode(code);
        product.setDescription(code + "description");
        product.setValid(true);
        product.setStock(1);
        product.setPrice(100);
        product.setCurrency(Currencies.RON);

        return product;
    }




    private User getUserWithRole(Roles role) {
        User user = new User();
        user.setFirstname("firstname");
        user.setRoles(Collections.singletonList(role));
        user.setId(1L);

        Address address = new Address();
        address.setCity("Bucuresti");
        address.setNumber(1);
        address.setStreet("Victoriei");
        address.setZipcode("123");

        user.setAddress(address);
        userRepository.save(user);

        return user;

    }


}
