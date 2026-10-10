package com.agrirent.controller;

import com.agrirent.dto.ReviewRequest;
import com.agrirent.dto.ReviewResponse;
import com.agrirent.security.UserPrincipal;
import com.agrirent.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for equipment ratings and reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(summary = "Get reviews for specific equipment")
    public ResponseEntity<List<ReviewResponse>> getEquipmentReviews(@PathVariable Long equipmentId) {
        List<ReviewResponse> reviews = reviewService.getReviewsForEquipment(equipmentId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/equipment/{equipmentId}")
    @Operation(summary = "Submit a rating & review for equipment")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long equipmentId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        ReviewResponse response = reviewService.addReview(equipmentId, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
