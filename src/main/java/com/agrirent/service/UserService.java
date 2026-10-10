package com.agrirent.service;

import com.agrirent.dto.UserResponse;
import com.agrirent.entity.Role;
import com.agrirent.entity.User;
import com.agrirent.exception.ResourceNotFoundException;
import com.agrirent.repository.UserRepository;
import com.agrirent.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToResponse(user);
    }

    public UserResponse getCurrentUserProfile(UserPrincipal currentUser) {
        return getUserProfile(currentUser.getId());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUserProfile(Long id, UserResponse request, UserPrincipal currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new org.springframework.security.access.AccessDeniedException("Cannot update another user's profile");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getState() != null) {
            user.setState(request.getState());
        }
        if (request.getPincode() != null) {
            user.setPincode(request.getPincode());
        }

        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    public UserResponse mapToResponse(User u) {
        UserResponse res = new UserResponse();
        res.setId(u.getId());
        res.setName(u.getName());
        res.setEmail(u.getEmail());
        res.setRole(u.getRole());
        res.setPhone(u.getPhone());
        res.setAddress(u.getAddress());
        res.setCity(u.getCity());
        res.setState(u.getState());
        res.setPincode(u.getPincode());
        res.setCreatedAt(u.getCreatedAt());
        return res;
    }
}
