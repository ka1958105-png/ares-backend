package com.agrirent.service;

import com.agrirent.dto.EquipmentRequest;
import com.agrirent.dto.EquipmentResponse;
import com.agrirent.entity.Equipment;
import com.agrirent.entity.EquipmentCategory;
import com.agrirent.entity.Role;
import com.agrirent.entity.Review;
import com.agrirent.entity.User;
import com.agrirent.exception.BadRequestException;
import com.agrirent.exception.ResourceNotFoundException;
import com.agrirent.repository.EquipmentRepository;
import com.agrirent.repository.ReviewRepository;
import com.agrirent.repository.UserRepository;
import com.agrirent.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public EquipmentService(EquipmentRepository equipmentRepository,
                            UserRepository userRepository,
                            ReviewRepository reviewRepository) {
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<EquipmentResponse> getAllEquipment(EquipmentCategory category, String city, String keyword) {
        List<Equipment> list;
        if (category == null && (city == null || city.isBlank()) && (keyword == null || keyword.isBlank())) {
            list = equipmentRepository.findByIsAvailableTrue();
        } else {
            list = equipmentRepository.searchEquipment(category, city, keyword);
        }
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public EquipmentResponse getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));
        return mapToResponse(equipment);
    }

    public List<EquipmentResponse> getEquipmentByOwner(Long ownerId) {
        return equipmentRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EquipmentResponse createEquipment(EquipmentRequest request, UserPrincipal currentUser) {
        User owner = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        if (owner.getRole() != Role.ROLE_OWNER && owner.getRole() != Role.ROLE_ADMIN) {
            throw new BadRequestException("Only registered equipment owners or admins can list machinery");
        }

        Equipment equipment = new Equipment();
        populateEquipmentFields(equipment, request);
        equipment.setOwner(owner);

        Equipment saved = equipmentRepository.save(equipment);
        return mapToResponse(saved);
    }

    @Transactional
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request, UserPrincipal currentUser) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));

        validateOwnership(equipment, currentUser);
        populateEquipmentFields(equipment, request);

        Equipment updated = equipmentRepository.save(equipment);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteEquipment(Long id, UserPrincipal currentUser) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));

        validateOwnership(equipment, currentUser);
        equipmentRepository.delete(equipment);
    }

    @Transactional
    public EquipmentResponse toggleAvailability(Long id, UserPrincipal currentUser) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));

        validateOwnership(equipment, currentUser);
        equipment.setIsAvailable(!equipment.getIsAvailable());
        Equipment saved = equipmentRepository.save(equipment);
        return mapToResponse(saved);
    }

    private void validateOwnership(Equipment equipment, UserPrincipal currentUser) {
        if (!equipment.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("You do not have permission to manage this equipment");
        }
    }

    private void populateEquipmentFields(Equipment equipment, EquipmentRequest req) {
        equipment.setTitle(req.getTitle());
        equipment.setDescription(req.getDescription());
        equipment.setCategory(req.getCategory());
        equipment.setModelYear(req.getModelYear());
        equipment.setHorsepower(req.getHorsepower());
        equipment.setFuelType(req.getFuelType());
        equipment.setHourlyRate(req.getHourlyRate());
        equipment.setDailyRate(req.getDailyRate());
        equipment.setSecurityDeposit(req.getSecurityDeposit());
        equipment.setLocation(req.getLocation());
        equipment.setCity(req.getCity());
        equipment.setState(req.getState());
        equipment.setPincode(req.getPincode());
        equipment.setImageUrl(req.getImageUrl());
        if (req.getIsAvailable() != null) {
            equipment.setIsAvailable(req.getIsAvailable());
        }
        equipment.setEquipmentCondition(req.getEquipmentCondition());
    }

    public EquipmentResponse mapToResponse(Equipment eq) {
        EquipmentResponse res = new EquipmentResponse();
        res.setId(eq.getId());
        res.setTitle(eq.getTitle());
        res.setDescription(eq.getDescription());
        res.setCategory(eq.getCategory());
        res.setModelYear(eq.getModelYear());
        res.setHorsepower(eq.getHorsepower());
        res.setFuelType(eq.getFuelType());
        res.setHourlyRate(eq.getHourlyRate());
        res.setDailyRate(eq.getDailyRate());
        res.setSecurityDeposit(eq.getSecurityDeposit());
        res.setLocation(eq.getLocation());
        res.setCity(eq.getCity());
        res.setState(eq.getState());
        res.setPincode(eq.getPincode());
        res.setImageUrl(eq.getImageUrl());
        res.setIsAvailable(eq.getIsAvailable());
        res.setEquipmentCondition(eq.getEquipmentCondition());
        res.setCreatedAt(eq.getCreatedAt());

        if (eq.getOwner() != null) {
            res.setOwnerId(eq.getOwner().getId());
            res.setOwnerName(eq.getOwner().getName());
            res.setOwnerPhone(eq.getOwner().getPhone());
            res.setOwnerEmail(eq.getOwner().getEmail());
        }

        // Ratings & reviews stats
        List<Review> reviews = reviewRepository.findByEquipmentIdOrderByCreatedAtDesc(eq.getId());
        if (!reviews.isEmpty()) {
            double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
            res.setAverageRating(Math.round(avg * 10.0) / 10.0);
            res.setTotalReviews(reviews.size());
        } else {
            res.setAverageRating(0.0);
            res.setTotalReviews(0);
        }

        return res;
    }
}
