package com.agrirent.dto;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long equipmentId;
    private Long farmerId;
    private String farmerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, Long equipmentId, Long farmerId, String farmerName, Integer rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
