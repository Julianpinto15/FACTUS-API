package apiFactus.service;

import apiFactus.model.Product;
import apiFactus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Obtiene todos los productos
     * @return Lista de productos
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su ID
     * @param id ID del producto
     * @return Optional con el producto, o vacío si no existe
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Busca un producto por su código
     * @param code Código del producto
     * @return Producto encontrado o null si no existe
     */
    public Product getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    /**
     * Guarda un nuevo producto
     * @param product Producto a guardar
     * @return Producto guardado con ID asignado
     */
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productDetails Nuevos datos del producto
     * @return Producto actualizado
     * @throws RuntimeException si el producto no existe
     */
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Actualizar propiedades
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setCode(productDetails.getCode());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setTaxRate(productDetails.getTaxRate());
                    existingProduct.setUnitMeasureId(productDetails.getUnitMeasureId());
                    existingProduct.setStandardCodeId(productDetails.getStandardCodeId());
                    existingProduct.setTributeId(productDetails.getTributeId());
                    existingProduct.setIsExcluded(productDetails.getIsExcluded());

                    // Si hay más propiedades en tu modelo, agrégalas aquí

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productRepository.delete(product);
    }
}