package com.back.routopia.controller;

import com.back.routopia.entity.AppSettings;
import com.back.routopia.service.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Admin Settings", description = "Configuración de la plataforma (solo ADMIN)")
public class AdminSettingsController {

    @Autowired
    private AppSettingsService appSettingsService;

    @Operation(summary = "Ver la configuración actual (sin exponer la contraseña SMTP)")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSettings() {
        AppSettings settings = appSettingsService.getSettings();
        boolean smtpConfigured = settings.getSmtpEmail() != null && !settings.getSmtpEmail().isBlank()
                && settings.getSmtpAppPassword() != null && !settings.getSmtpAppPassword().isBlank();

        return ResponseEntity.ok(Map.of(
                "whatsappNumber", settings.getWhatsappNumber() != null ? settings.getWhatsappNumber() : "",
                "whatsappMessage", settings.getWhatsappMessage() != null ? settings.getWhatsappMessage() : "",
                "smtpEmail", settings.getSmtpEmail() != null ? settings.getSmtpEmail() : "",
                "smtpConfigured", smtpConfigured
        ));
    }

    @Operation(summary = "Actualizar el número y mensaje por defecto de WhatsApp")
    @PutMapping("/whatsapp")
    public ResponseEntity<Map<String, String>> updateWhatsappSettings(
            @RequestBody Map<String, String> body
    ) {
        AppSettings settings = appSettingsService.updateWhatsappSettings(
                body.get("whatsappNumber"),
                body.get("whatsappMessage")
        );
        return ResponseEntity.ok(Map.of(
                "whatsappNumber", settings.getWhatsappNumber(),
                "whatsappMessage", settings.getWhatsappMessage()
        ));
    }

    @Operation(summary = "Actualizar las credenciales SMTP (Gmail) para el envío de emails")
    @PutMapping("/email")
    public ResponseEntity<Map<String, Object>> updateEmailSettings(
            @RequestBody Map<String, String> body
    ) {
        AppSettings settings = appSettingsService.updateEmailSettings(
                body.get("smtpEmail"),
                body.get("smtpAppPassword")
        );
        return ResponseEntity.ok(Map.of(
                "smtpEmail", settings.getSmtpEmail(),
                "smtpConfigured", true
        ));
    }
}
