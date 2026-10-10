package com.agrirent.service;

import com.agrirent.dto.BookingRequest;
import com.agrirent.dto.BookingResponse;
import com.agrirent.dto.BookingStatusUpdateRequest;
import com.agrirent.entity.*;
import com.agrirent.exception.BadRequestException;
import com.agrirent.exception.ResourceNotFoundException;
import com.agrirent.repository.BookingRepository;
import com.agrirent.repository.EquipmentRepository;
import com.agrirent.repository.UserRepository;
import com.agrirent.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          EquipmentRepository equipmentRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, UserPrincipal currentUser) {
        User farmer = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", request.getEquipmentId()));

        if (!equipment.getIsAvailable()) {
            throw new BadRequestException("This equipment is currently marked as unavailable for rent");
        }

        if (equipment.getOwner().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot rent your own equipment");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        // Check date conflict with other approved or active bookings
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                equipment.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("This equipment is already reserved for the selected date range");
        }

        int durationUnits;
        BigDecimal totalCost;

        if (request.getRentalType() == RentalType.HOURLY) {
            durationUnits = request.getDurationUnits() != null && request.getDurationUnits() > 0
                    ? request.getDurationUnits() : 8; // default to 8 hours
            totalCost = equipment.getHourlyRate().multiply(BigDecimal.valueOf(durationUnits));
        } else {
            long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
            durationUnits = (int) Math.max(1, days);
            totalCost = equipment.getDailyRate().multiply(BigDecimal.valueOf(durationUnits));
        }

        Booking booking = new Booking();
        booking.setEquipment(equipment);
        booking.setFarmer(farmer);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setRentalType(request.getRentalType());
        booking.setDurationUnits(durationUnits);
        booking.setTotalCost(totalCost);
        booking.setSecurityDeposit(equipment.getSecurityDeposit());
        booking.setStatus(BookingStatus.PENDING);
        booking.setDeliveryRequired(request.getDeliveryRequired());
        booking.setDeliveryAddress(request.getDeliveryAddress());
        booking.setNotes(request.getNotes());

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public List<BookingResponse> getFarmerBookings(Long farmerId) {
        return bookingRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getOwnerBookings(Long ownerId) {
        return bookingRepository.findByEquipmentOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id, UserPrincipal currentUser) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        boolean isFarmer = booking.getFarmer().getId().equals(currentUser.getId());
        boolean isOwner = booking.getEquipment().getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;

        if (!isFarmer && !isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to view this booking");
        }

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse updateBookingStatus(Long id, BookingStatusUpdateRequest request, UserPrincipal currentUser) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        boolean isOwner = booking.getEquipment().getOwner().getId().equals(currentUser.getId());
        boolean isFarmer = booking.getFarmer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;

        BookingStatus newStatus = request.getStatus();

        // Farmer can only cancel if it's pending or approved
        if (isFarmer && !isOwner && !isAdmin) {
            if (newStatus == BookingStatus.CANCELLED) {
                if (booking.getStatus() == BookingStatus.ACTIVE || booking.getStatus() == BookingStatus.COMPLETED) {
                    throw new BadRequestException("Cannot cancel an active or completed rental");
                }
                booking.setStatus(BookingStatus.CANCELLED);
            } else {
                throw new AccessDeniedException("Farmers can only cancel pending/approved bookings");
            }
        } else if (isOwner || isAdmin) {
            // Owner / Admin can approve, reject, activate, complete, cancel
            booking.setStatus(newStatus);
        } else {
            throw new AccessDeniedException("You are not authorized to modify this booking");
        }

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public BookingResponse mapToResponse(Booking b) {
        BookingResponse res = new BookingResponse();
        res.setId(b.getId());
        res.setStartDate(b.getStartDate());
        res.setEndDate(b.getEndDate());
        res.setRentalType(b.getRentalType());
        res.setDurationUnits(b.getDurationUnits());
        res.setTotalCost(b.getTotalCost());
        res.setSecurityDeposit(b.getSecurityDeposit());
        res.setStatus(b.getStatus());
        res.setDeliveryRequired(b.getDeliveryRequired());
        res.setDeliveryAddress(b.getDeliveryAddress());
        res.setNotes(b.getNotes());
        res.setCreatedAt(b.getCreatedAt());

        if (b.getEquipment() != null) {
            res.setEquipmentId(b.getEquipment().getId());
            res.setEquipmentTitle(b.getEquipment().getTitle());
            res.setEquipmentImageUrl(b.getEquipment().getImageUrl());
            res.setEquipmentCategory(b.getEquipment().getCategory().name());
            res.setEquipmentDailyRate(b.getEquipment().getDailyRate());
            res.setEquipmentHourlyRate(b.getEquipment().getHourlyRate());

            if (b.getEquipment().getOwner() != null) {
                res.setOwnerId(b.getEquipment().getOwner().getId());
                res.setOwnerName(b.getEquipment().getOwner().getName());
                res.setOwnerPhone(b.getEquipment().getOwner().getPhone());
            }
        }

        if (b.getFarmer() != null) {
            res.setFarmerId(b.getFarmer().getId());
            res.setFarmerName(b.getFarmer().getName());
            res.setFarmerEmail(b.getFarmer().getEmail());
            res.setFarmerPhone(b.getFarmer().getPhone());
        }

        return res;
    }
}
