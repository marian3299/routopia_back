package com.back.routopia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Configuración global de la plataforma. Fila única (id fijo = 1), editable
 * desde el panel de superadministrador.
 */
@Entity
@Table(name = "app_settings")
public class AppSettings {

    @Id
    private Long id = 1L;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @Column(name = "whatsapp_message", length = 500)
    private String whatsappMessage;

    public Long getId() {
        return id;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getWhatsappMessage() {
        return whatsappMessage;
    }

    public void setWhatsappMessage(String whatsappMessage) {
        this.whatsappMessage = whatsappMessage;
    }
}
