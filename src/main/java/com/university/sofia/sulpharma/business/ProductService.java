package com.university.sofia.sulpharma.business;

import com.university.sofia.sulpharma.persistence.dto.ProductDTO;
import com.university.sofia.sulpharma.persistence.entity.Product;
import com.university.sofia.sulpharma.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private static final int PAGE_SIZE = 12;
    private final ModelMapper modelMapper;
    private static final String PRODUCT_NOT_FOUND = "Product with id %s is not found";


    /**
     * Create product product dto.
     *
     * @param productDTO the product dto input
     * @return the created product dto
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product = productRepository.save(product);
        log.info("Created product with id {}", product.getId());
        return modelMapper.map(product, ProductDTO.class);
    }

    /**
     * Find paginated products.
     * <p>
     * There are three logics here:
     * Search by category only
     * Search by category and subcategory
     * Search by query
     *
     * @param page                the page
     * @param optionalCategory    the optional category
     * @param optionalSubcategory the optional subcategory
     * @param optionalQuery       the optional query
     * @param sortBy              the sort by
     * @param order               the order
     * @return the page
     */
    public Page<ProductDTO> findPaginated(
            int page,
            Optional<String> optionalCategory,
            Optional<String> optionalSubcategory,
            Optional<String> optionalQuery,
            String sortBy,
            String order
    ) {
        log.info("Fetch all products");
        Sort.Direction direction = order.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        if (optionalCategory.isPresent()) {
            Product.Category category = Product.Category.fromLabel(optionalCategory.get());
            if (optionalSubcategory.isPresent()) {
                Product.Subcategory subcategory = Product.Subcategory.fromLabel(optionalSubcategory.get());
                return productRepository.findAllByCategoryAndSubcategory(PageRequest.of(page, PAGE_SIZE, sort),
                        category,
                        subcategory)
                        .map(product -> modelMapper.map(product, ProductDTO.class));
            }

            return productRepository.findAllByCategory(PageRequest.of(page, PAGE_SIZE, sort), category)
                    .map(product -> modelMapper.map(product, ProductDTO.class));
        }

        if (optionalQuery.isPresent()) {
            String query = optionalQuery.get();
            return productRepository.findAllByDescriptionContaining(PageRequest.of(page, PAGE_SIZE, sort), query)
                    .map(product -> modelMapper.map(product, ProductDTO.class));

        }

        return productRepository.findAll(PageRequest.of(page, PAGE_SIZE, sort))
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    /**
     * Gets product by id.
     *
     * @param productId the product id
     * @return the product by id
     */
    public ProductDTO getProductById(Long productId) {
        log.info("Get Product by id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
        return modelMapper.map(product, ProductDTO.class);
    }

    /**
     * Update product by id.
     *
     * @param productId         the product id
     * @param updatedProductDTO the updated product dto
     * @return the updated product dto
     */
    public ProductDTO updateProductById(Long productId, ProductDTO updatedProductDTO) {
        log.info("Start updating product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));

        product.update(modelMapper.map(updatedProductDTO, Product.class));

        productRepository.save(product);
        log.info("Updated product with id: {}", productId);

        return modelMapper.map(product, ProductDTO.class);
    }

    /**
     * Delete product by id.
     *
     * @param productId the product id
     * @return the deleted product dto
     */
    public ProductDTO deleteProductById(Long productId) {
        log.info("Start deleting product with id {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
        productRepository.delete(product);
        log.info("Deleted product with id {}", productId);
        return modelMapper.map(product, ProductDTO.class);
    }
}