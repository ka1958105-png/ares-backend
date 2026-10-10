package com.agrirent.controller;

import com.agrirent.dto.DashboardStatsResponse;
import com.agrirent.security.UserPrincipal;
import com.agrirent.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints for user and owner analytics")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Get analytics and revenue metrics for the logged-in equipment owner")
    public ResponseEntity<DashboardStatsResponse> getOwnerStats(@AuthenticationPrincipal UserPrincipal currentUser) {
        DashboardStatsResponse stats = dashboardService.getOwnerStats(currentUser.getId());
        return ResponseEntity.ok(stats);
    }
}
