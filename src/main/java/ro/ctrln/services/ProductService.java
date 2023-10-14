package ro.ctrln.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.mappers.ProductMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;


    private final ProductMapper productMapper;


    public ProductDTO getProduct(String productCode) throws InvalidProductCodeException {
        Product product = getProductEntity(productCode);
        return productMapper.toDTO(product);

    }


    public void addProduct(ProductDTO productDTO, Long customerId) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    public void updateProduct(ProductDTO productDTO, Long customerId) throws InvalidProductCodeException {

        if (productDTO == null || productDTO.getCode() == null) {
            throw new InvalidProductCodeException();
        }

        log.info("Customer with ID {} is trying to update the product {}", customerId, productDTO.getCode());

        Product product = getProductEntity(productDTO.getCode());
        product.setValid(productDTO.isValid());
        product.setStock(productDTO.getStock());
        product.setPrice(productDTO.getPrice());
        product.setCurrency(productDTO.getCurrency());
        product.setDescription(productDTO.getDescription());

        productRepository.save(product);
    }


    public void deleteProduct(String productCode, Long customerId) throws InvalidProductCodeException {
        Product product = getProductEntity(productCode);
        productRepository.delete(product);
    }


    private Product getProductEntity(String productCode) throws InvalidProductCodeException {
        Optional<Product> product = productRepository.findByCode(productCode);
        if (!product.isPresent()) {
            throw new InvalidProductCodeException();
        }
        return product.get();
    }

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    public void addStock(String productCode, Integer quantity, Long customerId) throws InvalidProductCodeException {
        if (productCode == null) {
            throw new InvalidProductCodeException();
        }

        Product product = getProductEntity(productCode);
        product.setStock(product.getStock()+quantity);
        productRepository.save(product);
    }

}
