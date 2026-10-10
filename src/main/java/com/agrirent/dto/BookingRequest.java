package com.agrirent.dto;

import com.agrirent.entity.RentalType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingRequest {

    @NotNull(message = "Equipment ID is required")
    private Long equipmentId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    @NotNull(message = "Rental type is required (HOURLY or DAILY)")
    private RentalType rentalType;

    // For HOURLY, number of hours (e.g., 4, 8); for DAILY, calculated from dates or specified
    private Integer durationUnits;

    private Boolean deliveryRequired = false;
    private String deliveryAddress;
    private String notes;

    public BookingRequest() {
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(RentalType rentalType) {
        this.rentalType = rentalType;
    }

    public Integer getDurationUnits() {
        return durationUnits;
    }

    public void setDurationUnits(Integer durationUnits) {
        this.durationUnits = durationUnits;
    }

    public Boolean getDeliveryRequired() {
        return deliveryRequired;
    }

    public void setDeliveryRequired(Boolean deliveryRequired) {
        this.deliveryRequired = deliveryRequired;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
