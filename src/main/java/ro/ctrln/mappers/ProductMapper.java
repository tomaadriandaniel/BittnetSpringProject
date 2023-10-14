package ro.ctrln.mappers;


import org.springframework.stereotype.Component;
import ro.ctrln.entities.Product;
import ro.ctrln.dtos.ProductDTO;

@Component
public class ProductMapper {

    public ProductDTO toDTO (Product product){
        if(product == null){
            return null;
        }


        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCode(product.getCode());
        productDTO.setCurrency(product.getCurrency());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setValid(product.isValid());
        return productDTO;



    }

    public Product toEntity(ProductDTO productDTO) {
        if(productDTO == null){
            return null;
        }

        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setCurrency(productDTO.getCurrency());
        product.setDescription(productDTO.getDescription());
        product.setId(productDTO.getId());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setValid(productDTO.isValid());

        return product;
    }
}
