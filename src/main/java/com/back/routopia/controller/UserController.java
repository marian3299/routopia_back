package com.back.routopia.controller;

import com.back.routopia.dto.UserDTO;
import com.back.routopia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "User management APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users except ADMIN")
    public ResponseEntity<List<UserDTO>> getAllUsersExceptAdmin() {
        List<UserDTO> users = userService.getAllUsersExceptAdmin();
        return ResponseEntity.ok(users);
    }
}

