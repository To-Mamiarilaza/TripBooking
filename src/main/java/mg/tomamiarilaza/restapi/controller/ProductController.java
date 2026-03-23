package mg.tomamiarilaza.restapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mg.tomamiarilaza.restapi.dto.ProductDTO;
import mg.tomamiarilaza.restapi.service.ProductService;
import mg.tomamiarilaza.restapi.service.TokenService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Object create(
            @RequestHeader(value = "Authorization", required = false) String header,
            @Valid @RequestBody ProductDTO product) {

        if (header == null) {
            return Map.of("message", "Token missing");
        }

        String token = header.replace("Bearer ", "");

        if (!tokenService.isValid(token)) {
            return Map.of("message", "Token invalid");
        }

        return service.save(product);
    }

}
