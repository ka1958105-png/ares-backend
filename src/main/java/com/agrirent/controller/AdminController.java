package com.agrirent.controller;

import com.agrirent.dto.BookingResponse;
import com.agrirent.dto.DashboardStatsResponse;
import com.agrirent.dto.UserResponse;
import com.agrirent.service.BookingService;
import com.agrirent.service.DashboardService;
import com.agrirent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Endpoints reserved for administrators")
public class AdminController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final BookingService bookingService;

    public AdminController(DashboardService dashboardService,
                           UserService userService,
                           BookingService bookingService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get platform-wide analytics and statistics")
    public ResponseEntity<DashboardStatsResponse> getAdminStats() {
        return ResponseEntity.ok(dashboardService.getAdminStats());
    }

    @GetMapping("/users")
    @Operation(summary = "Get list of all registered users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all platform bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}
