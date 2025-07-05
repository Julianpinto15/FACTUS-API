package apiFactus.service;

import apiFactus.model.Product;
import apiFactus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Map<String, Object> getPaginatedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", productPage.getContent());
        response.put("total", productPage.getTotalElements());
        return response;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setCode(productDetails.getCode());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setTaxRate(productDetails.getTaxRate());
                    existingProduct.setUnitMeasureId(productDetails.getUnitMeasureId());
                    existingProduct.setStandardCodeId(productDetails.getStandardCodeId());
                    existingProduct.setTributeId(productDetails.getTributeId());
                    existingProduct.setExcluded(productDetails.getExcluded());

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        productRepository.delete(product);
    }
}
