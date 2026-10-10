package com.agrirent.repository;

import com.agrirent.entity.Booking;
import com.agrirent.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    @Query("SELECT b FROM Booking b WHERE b.equipment.owner.id = :ownerId ORDER BY b.createdAt DESC")
    List<Booking> findByEquipmentOwnerIdOrderByCreatedAtDesc(@Param("ownerId") Long ownerId);

    List<Booking> findByEquipmentIdOrderByStartDateDesc(Long equipmentId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.equipment.id = :equipmentId " +
           "AND b.status IN ('APPROVED', 'ACTIVE') " +
           "AND ((b.startDate <= :endDate AND b.endDate >= :startDate))")
    List<Booking> findOverlappingBookings(
            @Param("equipmentId") Long equipmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    long countByStatus(BookingStatus status);
}
