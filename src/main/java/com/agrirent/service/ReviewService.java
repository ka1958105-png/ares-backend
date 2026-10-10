package com.agrirent.service;

import com.agrirent.dto.ReviewRequest;
import com.agrirent.dto.ReviewResponse;
import com.agrirent.entity.Equipment;
import com.agrirent.entity.Review;
import com.agrirent.entity.User;
import com.agrirent.exception.BadRequestException;
import com.agrirent.exception.ResourceNotFoundException;
import com.agrirent.repository.EquipmentRepository;
import com.agrirent.repository.ReviewRepository;
import com.agrirent.repository.UserRepository;
import com.agrirent.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         EquipmentRepository equipmentRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewResponse> getReviewsForEquipment(Long equipmentId) {
        return reviewRepository.findByEquipmentIdOrderByCreatedAtDesc(equipmentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse addReview(Long equipmentId, ReviewRequest request, UserPrincipal currentUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", equipmentId));

        if (equipment.getOwner().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Owners cannot review their own machinery");
        }

        User farmer = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Review review = new Review(equipment, farmer, request.getRating(), request.getComment());
        Review saved = reviewRepository.save(review);

        return mapToResponse(saved);
    }

    private ReviewResponse mapToResponse(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getEquipment().getId(),
                r.getFarmer().getId(),
                r.getFarmer().getName(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
