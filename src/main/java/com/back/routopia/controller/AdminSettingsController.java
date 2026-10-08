package com.back.routopia.controller;

import com.back.routopia.entity.AppSettings;
import com.back.routopia.service.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
}
