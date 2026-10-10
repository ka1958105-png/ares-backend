package com.agrirent.controller;

import com.agrirent.dto.BookingRequest;
import com.agrirent.dto.BookingResponse;
import com.agrirent.dto.BookingStatusUpdateRequest;
import com.agrirent.security.UserPrincipal;
import com.agrirent.service.BookingService;
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
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Endpoints for reserving machinery and managing rental life-cycle")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Book agriculture equipment")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        BookingResponse response = bookingService.createBooking(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @Operation(summary = "Get bookings placed by the logged-in farmer")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<BookingResponse> bookings = bookingService.getFarmerBookings(currentUser.getId());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Get booking requests received for equipment owned by logged-in user")
    public ResponseEntity<List<BookingResponse>> getOwnerBookings(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<BookingResponse> bookings = bookingService.getOwnerBookings(currentUser.getId());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        BookingResponse booking = bookingService.getBookingById(id, currentUser);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update booking status (Approve, Reject, Cancel, Activate, Complete)")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        BookingResponse response = bookingService.updateBookingStatus(id, request, currentUser);
        return ResponseEntity.ok(response);
    }
}
