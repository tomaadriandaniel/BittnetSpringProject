package ro.ctrln.repositories;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.entities.Product;
import ro.ctrln.enums.Currencies;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

//@RunWith(SpringRunner.class) // se foloseste doar dc testam cu junit 4
@DataJpaTest
public class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findByCode_whenCodeIsPresentInDB_shouldReturnTheProduct(){
        Product productOne = new Product();
        productOne.setDescription("A beautiful product");
        productOne.setCurrency(Currencies.USD);
        productOne.setPrice(100);
        productOne.setStock(2);
        productOne.setValid(true);
        productOne.setCode("ProductCodeOne");

        Product productTwo = new Product();
        productTwo.setDescription("A beautiful product");
        productTwo.setCurrency(Currencies.USD);
        productTwo.setPrice(100);
        productTwo.setStock(2);
        productTwo.setValid(true);
        productTwo.setCode("ProductCodeTwo");


        testEntityManager.persist(productOne);
        testEntityManager.persist(productTwo);
        testEntityManager.flush();


        Optional<Product> optionalProduct = productRepository.findByCode(productOne.getCode());
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getCode()).isEqualTo(productOne.getCode());

    }


    @Test
    public void findByCode_whenCodeIsNotPresentInDB_shouldReturnEmpty(){
        Optional<Product> optionalProduct = productRepository.findByCode("some code");
        assertThat(optionalProduct).isNotPresent();
    }
}
