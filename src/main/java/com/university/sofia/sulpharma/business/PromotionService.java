package com.university.sofia.sulpharma.business;

import com.university.sofia.sulpharma.persistence.dto.PromotionDTO;
import com.university.sofia.sulpharma.persistence.entity.Promotion;
import com.university.sofia.sulpharma.persistence.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * The Promotion service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private static final int PAGE_SIZE = 10;
    private final ModelMapper modelMapper;
    private static final String PRODUCT_NOT_FOUND = "Promotion with id %s is not found";


    /**
     * Create promotion.
     *
     * @param promotionDTO the promotion dto
     * @return the promotion dto
     */
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = modelMapper.map(promotionDTO, Promotion.class);
        promotion = promotionRepository.save(promotion);
        log.info("Created promotion with id {}", promotion.getId());
        return modelMapper.map(promotion, PromotionDTO.class);
    }

    /**
     * Find paginated promotions.
     *
     * @param page the page
     * @return the page
     */
    public Page<PromotionDTO> findPaginated(int page) {
        log.info("Fetch all promotions");
        return promotionRepository.findAll(PageRequest.of(page, PAGE_SIZE))
                .map(promotion -> modelMapper.map(promotion, PromotionDTO.class));
    }

    /**
     * Gets promotion by id.
     *
     * @param promotionId the promotion id
     * @return the promotion by id
     */
    public PromotionDTO getPromotionById(Long promotionId) {
        log.info("Get Promotion by id: {}", promotionId);
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, promotionId)));
        return modelMapper.map(promotion, PromotionDTO.class);
    }

    /**
     * Update promotion by id.
     *
     * @param promotionId         the promotion id
     * @param updatedPromotionDTO the updated promotion dto
     * @return the promotion dto
     */
    public PromotionDTO updatePromotionById(Long promotionId, PromotionDTO updatedPromotionDTO) {
        log.info("Start updating promotion with id: {}", promotionId);
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, promotionId)));

        promotion.update(modelMapper.map(updatedPromotionDTO, Promotion.class));

        promotionRepository.save(promotion);
        log.info("Updated promotion with id: {}", promotionId);

        return modelMapper.map(promotion, PromotionDTO.class);
    }

    /**
     * Delete promotion by id.
     *
     * @param promotionId the promotion id
     * @return the promotion dto
     */
    public PromotionDTO deletePromotionById(Long promotionId) {
        log.info("Start deleting promotion with id {}", promotionId);
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, promotionId)));
        promotionRepository.delete(promotion);
        log.info("Deleted promotion with id {}", promotionId);
        return modelMapper.map(promotion, PromotionDTO.class);
    }
}
