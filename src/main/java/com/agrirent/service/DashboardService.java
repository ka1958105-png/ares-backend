package com.agrirent.service;

import com.agrirent.dto.DashboardStatsResponse;
import com.agrirent.entity.Booking;
import com.agrirent.entity.BookingStatus;
import com.agrirent.entity.Role;
import com.agrirent.repository.BookingRepository;
import com.agrirent.repository.EquipmentRepository;
import com.agrirent.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final EquipmentRepository equipmentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DashboardService(EquipmentRepository equipmentRepository,
                            BookingRepository bookingRepository,
                            UserRepository userRepository) {
        this.equipmentRepository = equipmentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public DashboardStatsResponse getAdminStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        stats.setTotalEquipment(equipmentRepository.count());
        stats.setAvailableEquipment(equipmentRepository.findByIsAvailableTrue().size());

        List<Booking> allBookings = bookingRepository.findAll();
        stats.setTotalBookings(allBookings.size());
        stats.setPendingBookings(allBookings.stream().filter(b -> b.getStatus() == BookingStatus.PENDING).count());
        stats.setActiveBookings(allBookings.stream().filter(b -> b.getStatus() == BookingStatus.ACTIVE).count());
        stats.setCompletedBookings(allBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());

        BigDecimal revenue = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.ACTIVE)
                .map(Booking::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(revenue);

        stats.setTotalUsers(userRepository.count());
        stats.setTotalFarmers(userRepository.findByRole(Role.ROLE_FARMER).size());
        stats.setTotalOwners(userRepository.findByRole(Role.ROLE_OWNER).size());

        return stats;
    }

    public DashboardStatsResponse getOwnerStats(Long ownerId) {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        var ownerEquipment = equipmentRepository.findByOwnerId(ownerId);
        stats.setTotalEquipment(ownerEquipment.size());
        stats.setAvailableEquipment(ownerEquipment.stream().filter(e -> Boolean.TRUE.equals(e.getIsAvailable())).count());

        var ownerBookings = bookingRepository.findByEquipmentOwnerIdOrderByCreatedAtDesc(ownerId);
        stats.setTotalBookings(ownerBookings.size());
        stats.setPendingBookings(ownerBookings.stream().filter(b -> b.getStatus() == BookingStatus.PENDING).count());
        stats.setActiveBookings(ownerBookings.stream().filter(b -> b.getStatus() == BookingStatus.ACTIVE).count());
        stats.setCompletedBookings(ownerBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());

        BigDecimal revenue = ownerBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.ACTIVE)
                .map(Booking::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(revenue);

        return stats;
    }
}
