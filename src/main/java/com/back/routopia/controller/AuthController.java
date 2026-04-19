package com.back.routopia.controller;

import com.back.routopia.dto.AuthResponse;
import com.back.routopia.dto.LoginRequest;
import com.back.routopia.dto.RegisterRequest;
import com.back.routopia.entity.User;
import com.back.routopia.service.JwtService;
import com.back.routopia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            String token = jwtService.generateToken(user);

            AuthResponse response = new AuthResponse(
                    token,
                    user.getNombre(),
                    user.getApellido(),
                    user.getEmail(),
                    user.getRole(),
                    user.getPermissions()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            userService.updateLastLogin(user.getEmail());
            String token = jwtService.generateToken(user);

            AuthResponse response = new AuthResponse(
                    token,
                    user.getNombre(),
                    user.getApellido(),
                    user.getEmail(),
                    user.getRole(),
                    user.getPermissions()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(new AuthResponse(
                    null, // No enviamos el token de nuevo
                    user.getNombre(),
                    user.getApellido(),
                    user.getEmail(),
                    user.getRole(),
                    user.getPermissions()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
