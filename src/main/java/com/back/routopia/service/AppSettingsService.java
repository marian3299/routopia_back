package com.back.routopia.service;

import com.back.routopia.entity.AppSettings;
import com.back.routopia.repositroy.AppSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppSettingsService {

    private static final Long SETTINGS_ID = 1L;
    private static final String WHATSAPP_NUMBER_PATTERN = "^\\d{8,15}$";

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    public AppSettings getSettings() {
        return appSettingsRepository.findById(SETTINGS_ID)
                .orElseGet(() -> appSettingsRepository.save(new AppSettings()));
    }

    public AppSettings updateWhatsappSettings(String whatsappNumber, String whatsappMessage) {
        if (whatsappNumber == null || !whatsappNumber.matches(WHATSAPP_NUMBER_PATTERN)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El número debe tener entre 8 y 15 dígitos, en formato internacional, sin '+' ni espacios (código de país + número)."
            );
        }
        if (whatsappMessage == null || whatsappMessage.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mensaje por defecto no puede estar vacío");
        }

        AppSettings settings = getSettings();
        settings.setWhatsappNumber(whatsappNumber);
        settings.setWhatsappMessage(whatsappMessage.trim());
        return appSettingsRepository.save(settings);
    }
}
