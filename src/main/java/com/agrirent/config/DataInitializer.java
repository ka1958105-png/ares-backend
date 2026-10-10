package com.agrirent.config;

import com.agrirent.entity.*;
import com.agrirent.repository.BookingRepository;
import com.agrirent.repository.EquipmentRepository;
import com.agrirent.repository.ReviewRepository;
import com.agrirent.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                     EquipmentRepository equipmentRepository,
                                     BookingRepository bookingRepository,
                                     ReviewRepository reviewRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return; // Already seeded
            }

            // 1. Create Users
            User admin = new User(
                    "AgriRent Admin",
                    "admin@agrirent.com",
                    passwordEncoder.encode("admin123"),
                    "+91 9876543210",
                    "AgriRent HQ, Tech Park",
                    "New Delhi",
                    "Delhi",
                    "110001",
                    Role.ROLE_ADMIN
            );
            userRepository.save(admin);

            User owner1 = new User(
                    "Rajesh Kumar",
                    "rajesh.owner@agrirent.com",
                    passwordEncoder.encode("owner123"),
                    "+91 9811122334",
                    "Kisan Agro Center, GT Road",
                    "Ludhiana",
                    "Punjab",
                    "141001",
                    Role.ROLE_OWNER
            );
            userRepository.save(owner1);

            User owner2 = new User(
                    "Sunita Patel",
                    "sunita.owner@agrirent.com",
                    passwordEncoder.encode("owner123"),
                    "+91 9822233445",
                    "GreenFields Agro Equip, Ring Road",
                    "Surat",
                    "Gujarat",
                    "395001",
                    Role.ROLE_OWNER
            );
            userRepository.save(owner2);

            User farmer1 = new User(
                    "Ramesh Sharma",
                    "ramesh.farmer@agrirent.com",
                    passwordEncoder.encode("farmer123"),
                    "+91 9833344556",
                    "Village Pipli, Post Kalan",
                    "Karnal",
                    "Haryana",
                    "132001",
                    Role.ROLE_FARMER
            );
            userRepository.save(farmer1);

            User farmer2 = new User(
                    "Anita Verma",
                    "anita.farmer@agrirent.com",
                    passwordEncoder.encode("farmer123"),
                    "+91 9844455667",
                    "Farm Gate 4, Green Valley",
                    "Amritsar",
                    "Punjab",
                    "143001",
                    Role.ROLE_FARMER
            );
            userRepository.save(farmer2);

            // 2. Create Equipment
            Equipment eq1 = new Equipment();
            eq1.setTitle("Mahindra 575 DI 45HP Tractor");
            eq1.setDescription("Reliable 4-cylinder fuel-efficient tractor, perfect for tilling, hauling, and operating rotary tillers. Well-serviced and fully insured with power steering.");
            eq1.setCategory(EquipmentCategory.TRACTORS);
            eq1.setModelYear(2023);
            eq1.setHorsepower(45);
            eq1.setFuelType("Diesel");
            eq1.setHourlyRate(new BigDecimal("450.00"));
            eq1.setDailyRate(new BigDecimal("3200.00"));
            eq1.setSecurityDeposit(new BigDecimal("2000.00"));
            eq1.setLocation("GT Road Depot, Ludhiana");
            eq1.setCity("Ludhiana");
            eq1.setState("Punjab");
            eq1.setPincode("141001");
            eq1.setImageUrl("https://images.unsplash.com/photo-1592982537447-7440770cbfc9?auto=format&fit=crop&w=800&q=80");
            eq1.setIsAvailable(true);
            eq1.setEquipmentCondition("Excellent");
            eq1.setOwner(owner1);
            equipmentRepository.save(eq1);

            Equipment eq2 = new Equipment();
            eq2.setTitle("John Deere 5050 D 50HP Power Tractor");
            eq2.setDescription("High torque 50 HP tractor equipped with dual clutch, oil immersed brakes, and heavy-duty 3-point linkage. Ideal for heavy disc harrowing and laser levelling.");
            eq2.setCategory(EquipmentCategory.TRACTORS);
            eq2.setModelYear(2024);
            eq2.setHorsepower(50);
            eq2.setFuelType("Diesel");
            eq2.setHourlyRate(new BigDecimal("550.00"));
            eq2.setDailyRate(new BigDecimal("4000.00"));
            eq2.setSecurityDeposit(new BigDecimal("2500.00"));
            eq2.setLocation("Model Farm Hub, Karnal");
            eq2.setCity("Karnal");
            eq2.setState("Haryana");
            eq2.setPincode("132001");
            eq2.setImageUrl("https://images.unsplash.com/photo-1530267981375-f0de937f5f13?auto=format&fit=crop&w=800&q=80");
            eq2.setIsAvailable(true);
            eq2.setEquipmentCondition("Like New");
            eq2.setOwner(owner1);
            equipmentRepository.save(eq2);

            Equipment eq3 = new Equipment();
            eq3.setTitle("Kubota DC-68G Paddy & Wheat Combine Harvester");
            eq3.setDescription("High capacity rubber crawler combine harvester designed for wet paddy fields and wheat crops. Minimizes grain loss and delivers clean harvest with rapid throughput.");
            eq3.setCategory(EquipmentCategory.HARVESTING);
            eq3.setModelYear(2023);
            eq3.setHorsepower(68);
            eq3.setFuelType("Diesel");
            eq3.setHourlyRate(new BigDecimal("1400.00"));
            eq3.setDailyRate(new BigDecimal("10500.00"));
            eq3.setSecurityDeposit(new BigDecimal("5000.00"));
            eq3.setLocation("Amritsar Agro Hub, Bypass Road");
            eq3.setCity("Amritsar");
            eq3.setState("Punjab");
            eq3.setPincode("143001");
            eq3.setImageUrl("https://images.unsplash.com/photo-1586771107445-d3ca888129ff?auto=format&fit=crop&w=800&q=80");
            eq3.setIsAvailable(true);
            eq3.setEquipmentCondition("Excellent");
            eq3.setOwner(owner1);
            equipmentRepository.save(eq3);

            Equipment eq4 = new Equipment();
            eq4.setTitle("Shaktiman 7-Feet Heavy Rotary Tiller (Rotavator)");
            eq4.setDescription("Heavy-duty multi-speed gearbox rotavator with boron steel blades. Excellent soil pulverization, preparation of seed beds, and stubble incorporation.");
            eq4.setCategory(EquipmentCategory.TILLAGE);
            eq4.setModelYear(2023);
            eq4.setHorsepower(40);
            eq4.setFuelType("PTO Driven");
            eq4.setHourlyRate(new BigDecimal("250.00"));
            eq4.setDailyRate(new BigDecimal("1800.00"));
            eq4.setSecurityDeposit(new BigDecimal("1000.00"));
            eq4.setLocation("Surat Kisan Depot, Ring Road");
            eq4.setCity("Surat");
            eq4.setState("Gujarat");
            eq4.setPincode("395001");
            eq4.setImageUrl("https://images.unsplash.com/photo-1628352081506-83c43123ed6d?auto=format&fit=crop&w=800&q=80");
            eq4.setIsAvailable(true);
            eq4.setEquipmentCondition("Good");
            eq4.setOwner(owner2);
            equipmentRepository.save(eq4);

            Equipment eq5 = new Equipment();
            eq5.setTitle("Yanmar 6-Row Ride-On Rice Transplanter");
            eq5.setDescription("Precision seedling transplanter with automatic float leveling and uniform spacing. Saves 80% manual labour and ensures high yield paddy cultivation.");
            eq5.setCategory(EquipmentCategory.SOWING_PLANTING);
            eq5.setModelYear(2022);
            eq5.setHorsepower(19);
            eq5.setFuelType("Petrol/Diesel");
            eq5.setHourlyRate(new BigDecimal("400.00"));
            eq5.setDailyRate(new BigDecimal("2900.00"));
            eq5.setSecurityDeposit(new BigDecimal("1500.00"));
            eq5.setLocation("AgriTech Center, Kurukshetra");
            eq5.setCity("Kurukshetra");
            eq5.setState("Haryana");
            eq5.setPincode("136118");
            eq5.setImageUrl("https://images.unsplash.com/photo-1595974482597-4b8da8879bc5?auto=format&fit=crop&w=800&q=80");
            eq5.setIsAvailable(true);
            eq5.setEquipmentCondition("Excellent");
            eq5.setOwner(owner1);
            equipmentRepository.save(eq5);

            Equipment eq6 = new Equipment();
            eq6.setTitle("Falcon Tractor-Mounted 400L Boom Sprayer");
            eq6.setDescription("Wide-span 30-feet nozzle spray boom with pressure regulation. Perfect for chemical crop protection, weed control, and liquid fertilizer application.");
            eq6.setCategory(EquipmentCategory.SPRAYERS_PROTECTION);
            eq6.setModelYear(2023);
            eq6.setHorsepower(35);
            eq6.setFuelType("PTO Driven");
            eq6.setHourlyRate(new BigDecimal("180.00"));
            eq6.setDailyRate(new BigDecimal("1200.00"));
            eq6.setSecurityDeposit(new BigDecimal("800.00"));
            eq6.setLocation("GreenFields Center, Surat");
            eq6.setCity("Surat");
            eq6.setState("Gujarat");
            eq6.setPincode("395001");
            eq6.setImageUrl("https://images.unsplash.com/photo-1523741543316-beb7fc7023d8?auto=format&fit=crop&w=800&q=80");
            eq6.setIsAvailable(true);
            eq6.setEquipmentCondition("Like New");
            eq6.setOwner(owner2);
            equipmentRepository.save(eq6);

            Equipment eq7 = new Equipment();
            eq7.setTitle("Lemken 3-Bottom Reversible Hydraulic Disc Plough");
            eq7.setDescription("Heavy-duty disc plough for deep tillage in hard soil conditions. Hydraulic rollover mechanism provides quick reversal at field headlands.");
            eq7.setCategory(EquipmentCategory.TILLAGE);
            eq7.setModelYear(2023);
            eq7.setHorsepower(55);
            eq7.setFuelType("Tractor Mounted");
            eq7.setHourlyRate(new BigDecimal("280.00"));
            eq7.setDailyRate(new BigDecimal("2000.00"));
            eq7.setSecurityDeposit(new BigDecimal("1200.00"));
            eq7.setLocation("Central Machinery Yard, Bhatinda");
            eq7.setCity("Bhatinda");
            eq7.setState("Punjab");
            eq7.setPincode("151001");
            eq7.setImageUrl("https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=800&q=80");
            eq7.setIsAvailable(true);
            eq7.setEquipmentCondition("Good");
            eq7.setOwner(owner1);
            equipmentRepository.save(eq7);

            Equipment eq8 = new Equipment();
            eq8.setTitle("Solar Submersible 5HP Irrigation Pump System");
            eq8.setDescription("Mobile solar pump set with portable panels and inverter. Delivers up to 45,000 liters/hour water flow without electricity or diesel cost.");
            eq8.setCategory(EquipmentCategory.IRRIGATION);
            eq8.setModelYear(2024);
            eq8.setHorsepower(5);
            eq8.setFuelType("Solar / Electric");
            eq8.setHourlyRate(new BigDecimal("200.00"));
            eq8.setDailyRate(new BigDecimal("1500.00"));
            eq8.setSecurityDeposit(new BigDecimal("1000.00"));
            eq8.setLocation("Kisan Urja Hub, Rajkot");
            eq8.setCity("Rajkot");
            eq8.setState("Gujarat");
            eq8.setPincode("360001");
            eq8.setImageUrl("https://images.unsplash.com/photo-1509391365360-2e959784a276?auto=format&fit=crop&w=800&q=80");
            eq8.setIsAvailable(true);
            eq8.setEquipmentCondition("Like New");
            eq8.setOwner(owner2);
            equipmentRepository.save(eq8);

            // 3. Create Bookings
            Booking b1 = new Booking();
            b1.setEquipment(eq1);
            b1.setFarmer(farmer1);
            b1.setStartDate(LocalDate.now().minusDays(5));
            b1.setEndDate(LocalDate.now().minusDays(3));
            b1.setRentalType(RentalType.DAILY);
            b1.setDurationUnits(3);
            b1.setTotalCost(new BigDecimal("9600.00"));
            b1.setSecurityDeposit(eq1.getSecurityDeposit());
            b1.setStatus(BookingStatus.COMPLETED);
            b1.setDeliveryRequired(true);
            b1.setDeliveryAddress("Farm field 12, Village Pipli, Karnal");
            b1.setNotes("Delivered promptly and returned in good condition");
            bookingRepository.save(b1);

            Booking b2 = new Booking();
            b2.setEquipment(eq2);
            b2.setFarmer(farmer2);
            b2.setStartDate(LocalDate.now().minusDays(1));
            b2.setEndDate(LocalDate.now().plusDays(2));
            b2.setRentalType(RentalType.DAILY);
            b2.setDurationUnits(4);
            b2.setTotalCost(new BigDecimal("16000.00"));
            b2.setSecurityDeposit(eq2.getSecurityDeposit());
            b2.setStatus(BookingStatus.ACTIVE);
            b2.setDeliveryRequired(false);
            b2.setNotes("Currently in active field operation for wheat sowing");
            bookingRepository.save(b2);

            Booking b3 = new Booking();
            b3.setEquipment(eq4);
            b3.setFarmer(farmer1);
            b3.setStartDate(LocalDate.now().plusDays(3));
            b3.setEndDate(LocalDate.now().plusDays(4));
            b3.setRentalType(RentalType.DAILY);
            b3.setDurationUnits(2);
            b3.setTotalCost(new BigDecimal("3600.00"));
            b3.setSecurityDeposit(eq4.getSecurityDeposit());
            b3.setStatus(BookingStatus.APPROVED);
            b3.setDeliveryRequired(true);
            b3.setDeliveryAddress("Pipli Agro Center");
            b3.setNotes("Scheduled for upcoming seed bed leveling");
            bookingRepository.save(b3);

            Booking b4 = new Booking();
            b4.setEquipment(eq3);
            b4.setFarmer(farmer2);
            b4.setStartDate(LocalDate.now().plusDays(7));
            b4.setEndDate(LocalDate.now().plusDays(8));
            b4.setRentalType(RentalType.DAILY);
            b4.setDurationUnits(2);
            b4.setTotalCost(new BigDecimal("21000.00"));
            b4.setSecurityDeposit(eq3.getSecurityDeposit());
            b4.setStatus(BookingStatus.PENDING);
            b4.setDeliveryRequired(true);
            b4.setDeliveryAddress("Green Valley Fields, Amritsar");
            b4.setNotes("Paddy harvesting requirement");
            bookingRepository.save(b4);

            // 4. Create Reviews
            Review r1 = new Review(
                    eq1,
                    farmer1,
                    5,
                    "The Mahindra 575 DI performed exceptionally well! Low diesel consumption and very smooth hydraulics for rotavator work. Rajesh was very cooperative."
            );
            reviewRepository.save(r1);

            Review r2 = new Review(
                    eq2,
                    farmer2,
                    5,
                    "Outstanding tractor power. Cleared 15 acres of tough stubble in record time. Clean machine and hassle-free pickup."
            );
            reviewRepository.save(r2);

            Review r3 = new Review(
                    eq4,
                    farmer1,
                    4,
                    "Good rotavator condition, sharp blades. Prepared the soil in a single pass. Highly recommended for cotton and wheat fields."
            );
            reviewRepository.save(r3);

            System.out.println(">>> AgriRent sample data initialized successfully!");
        };
    }
}
