package com.back.routopia.dto;

import com.back.routopia.entity.Role;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String nombre;
    private String apellido;
    private String email;
    private Role role;

    public AuthResponse(String token, String nombre, String apellido, String email, Role role) {
        this.token = token;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
