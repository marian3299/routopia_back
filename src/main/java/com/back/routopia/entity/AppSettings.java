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

    @Column(name = "smtp_email")
    private String smtpEmail;

    // Contraseña de aplicación de Gmail (no la contraseña normal de la cuenta).
    @Column(name = "smtp_app_password")
    private String smtpAppPassword;

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

    public String getSmtpEmail() {
        return smtpEmail;
    }

    public void setSmtpEmail(String smtpEmail) {
        this.smtpEmail = smtpEmail;
    }

    public String getSmtpAppPassword() {
        return smtpAppPassword;
    }

    public void setSmtpAppPassword(String smtpAppPassword) {
        this.smtpAppPassword = smtpAppPassword;
    }
}
