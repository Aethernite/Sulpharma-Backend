package com.university.sofia.sulpharma.presentation.controller;

import com.university.sofia.sulpharma.business.PromotionService;
import com.university.sofia.sulpharma.persistence.dto.PromotionDTO;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/promotions")
@CrossOrigin
public class PromotionController {

    private final PromotionService promotionService;

    /**
     * Create promotion response entity.
     *
     * @param promotionDTO the promotion dto
     * @return the response entity
     */
    @Operation(summary = "This request method is used for creating new promotion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promotion is created successfully"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody @Valid PromotionDTO promotionDTO) {
        return new ResponseEntity<>(promotionService.createPromotion(promotionDTO), HttpStatus.CREATED);
    }

    /**
     * Get all response entity.
     *
     * @return the response entity
     */
    @Operation(summary = "This request method return all of our promotions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return page of promotions"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(params = {"page"})
    public ResponseEntity<Page<PromotionDTO>> getAll(@RequestParam("page") int page) {
        return new ResponseEntity<>(promotionService.findPaginated(page), HttpStatus.OK);
    }

    /**
     * Gets promotion by id.
     *
     * @param promotionId the promotion id
     * @return the promotion by id
     */
    @Operation(summary = "This request method return promotion by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return promotion"),
            @ApiResponse(responseCode = "404", description = "Promotion not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable("promotionId") @Min(value = 1) Long promotionId) {
        return new ResponseEntity<>(promotionService.getPromotionById(promotionId), HttpStatus.OK);
    }

    /**
     * Update promotion by id response entity.
     *
     * @param promotionId  the promotion id
     * @param promotionDTO the promotion dto
     * @return the response entity
     */
    @Operation(summary = "This request method update promotion by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return updated promotion"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "404", description = "Promotiocccccccccccccccccccccccccn not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionDTO> updatePromotionById(@PathVariable("promotionId") @Min(value = 1) @NotNull Long promotionId, @Valid @RequestBody PromotionDTO promotionDTO) {
        return new ResponseEntity<>(promotionService.updatePromotionById(promotionId, promotionDTO), HttpStatus.OK);
    }

    /**
     * Delete promotion by id response entity.
     *
     * @param promotionId the promotion id
     * @return the response entity
     */
    @Operation(summary = "This request method delete promotion by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted promotion"),
            @ApiResponse(responseCode = "404", description = "Promotion not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping("/{promotionId}")
    public ResponseEntity<PromotionDTO> deletePromotionById(@PathVariable("promotionId") @Min(value = 1) @NotNull Long promotionId) {
        return new ResponseEntity<>(promotionService.deletePromotionById(promotionId), HttpStatus.OK);
    }

}

