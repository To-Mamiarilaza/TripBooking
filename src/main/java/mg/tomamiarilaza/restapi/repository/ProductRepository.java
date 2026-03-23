package mg.tomamiarilaza.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.tomamiarilaza.restapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
