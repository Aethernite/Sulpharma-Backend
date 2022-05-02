package com.university.sofia.sulpharma.presentation.controller;

import com.university.sofia.sulpharma.business.ProductService;
import com.university.sofia.sulpharma.persistence.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    /**
     * Create product response entity.
     *
     * @param productDTO the product dto
     * @return the response entity
     */
    @Operation(summary = "This request method is used for creating new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product is created successfully"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    /**
     * Get all response entity.
     *
     * @return the response entity
     */
    @Operation(summary = "This request method return all of our products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return page of products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(params = {"page"})
    public ResponseEntity<Page<ProductDTO>> search(
            @RequestParam("page") int page,
            @RequestParam("category") Optional<String> category,
            @RequestParam("subcategory") Optional<String> subcategory,
            @RequestParam("query") Optional<String> query,
            @RequestParam("sort") String sort,
            @RequestParam("order") String order
    ) {
        return new ResponseEntity<>(productService.findPaginated(page, category, subcategory, query, sort, order),
                HttpStatus.OK);
    }

    /**
     * Gets product by id.
     *
     * @param productId the product id
     * @return the product by id
     */
    @Operation(summary = "This request method return product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") @Min(value = 1) Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    /**
     * Update product by id response entity.
     *
     * @param productId  the product id
     * @param productDTO the product dto
     * @return the response entity
     */
    @Operation(summary = "This request method update product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return updated product"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable("productId") @Min(value = 1) @NotNull Long productId, @Valid @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.updateProductById(productId, productDTO), HttpStatus.OK);
    }

    /**
     * Delete product by id response entity.
     *
     * @param productId the product id
     * @return the response entity
     */
    @Operation(summary = "This request method delete product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable("productId") @Min(value = 1) @NotNull Long productId) {
        return new ResponseEntity<>(productService.deleteProductById(productId), HttpStatus.OK);
    }

}

