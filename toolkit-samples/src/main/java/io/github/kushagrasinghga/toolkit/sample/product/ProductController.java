package io.github.kushagrasinghga.toolkit.sample.product;

import io.github.kushagrasinghga.toolkit.sample.service.ProductService;
import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    Collection<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    Product findById(@PathVariable long id) {
        return productService.findById(id);
    }

    @PostMapping
    Product create(@RequestBody Product request) {
        return productService.create(request);
    }
}
