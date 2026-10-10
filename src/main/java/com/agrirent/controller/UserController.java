package com.agrirent.controller;

import com.agrirent.dto.UserResponse;
import com.agrirent.security.UserPrincipal;
import com.agrirent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user profile operations")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current logged-in user profile")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse response = userService.getCurrentUserProfile(currentUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current logged-in user profile")
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestBody UserResponse request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse response = userService.updateUserProfile(currentUser.getId(), request, currentUser);
        return ResponseEntity.ok(response);
    }
}
