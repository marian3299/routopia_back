package com.back.routopia.controller;

import com.back.routopia.entity.AppSettings;
import com.back.routopia.service.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Settings", description = "Configuración pública de la plataforma")
public class SettingsController {

    @Autowired
    private AppSettingsService appSettingsService;

    @Operation(summary = "Configuración de contacto por WhatsApp (lectura pública)")
    @GetMapping("/whatsapp")
    public ResponseEntity<Map<String, String>> getWhatsappSettings() {
        AppSettings settings = appSettingsService.getSettings();
        return ResponseEntity.ok(Map.of(
                "whatsappNumber", settings.getWhatsappNumber() != null ? settings.getWhatsappNumber() : "",
                "whatsappMessage", settings.getWhatsappMessage() != null ? settings.getWhatsappMessage() : ""
        ));
    }
}
