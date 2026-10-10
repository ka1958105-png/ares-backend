package com.agrirent.dto;

import com.agrirent.entity.BookingStatus;
import com.agrirent.entity.RentalType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private Long equipmentId;
    private String equipmentTitle;
    private String equipmentImageUrl;
    private String equipmentCategory;
    private BigDecimal equipmentDailyRate;
    private BigDecimal equipmentHourlyRate;

    private Long farmerId;
    private String farmerName;
    private String farmerEmail;
    private String farmerPhone;

    private Long ownerId;
    private String ownerName;
    private String ownerPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private RentalType rentalType;
    private Integer durationUnits;
    private BigDecimal totalCost;
    private BigDecimal securityDeposit;
    private BookingStatus status;
    private Boolean deliveryRequired;
    private String deliveryAddress;
    private String notes;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentTitle() {
        return equipmentTitle;
    }

    public void setEquipmentTitle(String equipmentTitle) {
        this.equipmentTitle = equipmentTitle;
    }

    public String getEquipmentImageUrl() {
        return equipmentImageUrl;
    }

    public void setEquipmentImageUrl(String equipmentImageUrl) {
        this.equipmentImageUrl = equipmentImageUrl;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public BigDecimal getEquipmentDailyRate() {
        return equipmentDailyRate;
    }

    public void setEquipmentDailyRate(BigDecimal equipmentDailyRate) {
        this.equipmentDailyRate = equipmentDailyRate;
    }

    public BigDecimal getEquipmentHourlyRate() {
        return equipmentHourlyRate;
    }

    public void setEquipmentHourlyRate(BigDecimal equipmentHourlyRate) {
        this.equipmentHourlyRate = equipmentHourlyRate;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        this.farmerPhone = farmerPhone;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
