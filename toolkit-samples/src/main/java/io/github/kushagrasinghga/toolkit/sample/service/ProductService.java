package io.github.kushagrasinghga.toolkit.sample.service;

import io.github.kushagrasinghga.toolkit.annotation.Audit;
import io.github.kushagrasinghga.toolkit.annotation.TrackExecution;
import io.github.kushagrasinghga.toolkit.dto.ErrorCode;
import io.github.kushagrasinghga.toolkit.exception.CustomException;
import io.github.kushagrasinghga.toolkit.sample.product.Product;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final AtomicLong ids = new AtomicLong();
    private final Map<Long, Product> products = new ConcurrentHashMap<>();

    public Collection<Product> findAll() {
        return products.values();
    }

    public Product findById(long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Product not found: " + id);
        }
        return product;
    }

    @Audit(action = "CREATE_PRODUCT")
    @TrackExecution
    public Product create(Product request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        long id = ids.incrementAndGet();
        Product product = new Product(id, request.name(), request.price());
        products.put(id, product);
        return product;
    }
}
