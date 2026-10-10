package com.agrirent.repository;

import com.agrirent.entity.Equipment;
import com.agrirent.entity.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByOwnerId(Long ownerId);

    List<Equipment> findByIsAvailableTrue();

    List<Equipment> findByCategoryAndIsAvailableTrue(EquipmentCategory category);

    @Query("SELECT e FROM Equipment e WHERE " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:city IS NULL OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Equipment> searchEquipment(
            @Param("category") EquipmentCategory category,
            @Param("city") String city,
            @Param("keyword") String keyword
    );
}
