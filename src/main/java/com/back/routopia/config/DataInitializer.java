package com.back.routopia.config;

import com.back.routopia.entity.Role;
import com.back.routopia.entity.User;
import com.back.routopia.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario administrador por defecto si no existe
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@routopia.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Admin user created:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
        }

        // Crear usuario normal de ejemplo si no existe
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@routopia.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);

            System.out.println("Normal user created:");
            System.out.println("Username: user");
            System.out.println("Password: user123");
        }
    }
}
