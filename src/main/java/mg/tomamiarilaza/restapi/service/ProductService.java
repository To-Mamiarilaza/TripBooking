package mg.tomamiarilaza.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.tomamiarilaza.restapi.dto.ProductDTO;
import mg.tomamiarilaza.restapi.model.Product;
import mg.tomamiarilaza.restapi.repository.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;

    public List<ProductDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow();
        return convertToDTO(product);
    }

    public ProductDTO save(ProductDTO product) {
        Product newProduct = repository.save(convertToEntity(product));
        return convertToDTO(newProduct);
    }

    public ProductDTO convertToDTO(Product product) {

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());

        return dto;
    }

    public Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        return product;
    }
}
