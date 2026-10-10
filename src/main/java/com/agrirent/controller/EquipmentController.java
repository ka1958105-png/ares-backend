package com.agrirent.controller;

import com.agrirent.dto.ApiResponse;
import com.agrirent.dto.EquipmentRequest;
import com.agrirent.dto.EquipmentResponse;
import com.agrirent.entity.EquipmentCategory;
import com.agrirent.security.UserPrincipal;
import com.agrirent.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@Tag(name = "Equipment", description = "Endpoints for browsing and managing agricultural machinery")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    @Operation(summary = "Browse or search available agricultural equipment")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment(
            @RequestParam(required = false) EquipmentCategory category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword) {
        List<EquipmentResponse> equipment = equipmentService.getAllEquipment(category, city, keyword);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment details by ID")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id) {
        EquipmentResponse response = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/my")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Get all equipment listed by currently logged in owner")
    public ResponseEntity<List<EquipmentResponse>> getMyEquipment(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<EquipmentResponse> equipment = equipmentService.getEquipmentByOwner(currentUser.getId());
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "List new agriculture equipment for rental")
    public ResponseEntity<EquipmentResponse> createEquipment(
            @Valid @RequestBody EquipmentRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        EquipmentResponse response = equipmentService.createEquipment(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Update equipment information")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        EquipmentResponse response = equipmentService.updateEquipment(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Toggle availability of equipment")
    public ResponseEntity<EquipmentResponse> toggleAvailability(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        EquipmentResponse response = equipmentService.toggleAvailability(id, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Delete equipment listing")
    public ResponseEntity<ApiResponse> deleteEquipment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        equipmentService.deleteEquipment(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Equipment listing deleted successfully"));
    }
}
