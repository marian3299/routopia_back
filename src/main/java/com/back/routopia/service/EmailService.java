package com.back.routopia.service;

import com.back.routopia.entity.AppSettings;
import com.back.routopia.entity.Booking;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

/**
 * Envía el email de confirmación de reserva usando las credenciales SMTP de Gmail
 * que carga el superadmin en /admin/settings (no credenciales estáticas de application.properties,
 * para que quien pruebe el proyecto pueda configurarlas sin tocar código).
 */
@Service
public class EmailService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "ES"));
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "ES"));

    @Autowired
    private AppSettingsService appSettingsService;

    @Async
    public void sendBookingConfirmation(Booking booking) {
        AppSettings settings = appSettingsService.getSettings();

        if (isBlank(settings.getSmtpEmail()) || isBlank(settings.getSmtpAppPassword())) {
            System.out.println("Envío de email omitido: SMTP no configurado desde /admin/settings.");
            return;
        }

        String recipient = booking.getUser().getEmail();
        if (isBlank(recipient)) {
            return;
        }

        try {
            JavaMailSenderImpl mailSender = buildMailSender(settings);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(settings.getSmtpEmail(), "Routopia");
            helper.setTo(recipient);
            helper.setSubject("Confirmación de tu reserva en Routopia");
            helper.setText(buildHtmlBody(booking, settings), true);

            mailSender.send(message);
            System.out.println("Email de confirmación enviado a " + recipient);
        } catch (Exception e) {
            // El envío de email nunca debe romper el flujo de creación de la reserva.
            System.err.println("Error enviando email de confirmación: " + e.getMessage());
        }
    }

    private JavaMailSenderImpl buildMailSender(AppSettings settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(settings.getSmtpEmail());
        mailSender.setPassword(settings.getSmtpAppPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }

    private String buildHtmlBody(Booking booking, AppSettings settings) {
        String destinoName = booking.getDestino().getName();
        String userName = booking.getUser().getNombre();
        String useDate = booking.getBookingDate().format(DATE_FORMATTER);
        String bookedAt = booking.getCreatedAt().format(DATETIME_FORMATTER);
        String whatsapp = settings.getWhatsappNumber();

        String notesRow = (!isBlank(booking.getNotes()))
                ? row("Comentarios", escape(booking.getNotes()))
                : "";
        String contactRow = (!isBlank(whatsapp))
                ? row("Contacto del proveedor", "WhatsApp: +" + escape(whatsapp))
                : "";

        return "<!DOCTYPE html>"
                + "<html><body style=\"margin:0;padding:0;background-color:#f7f5f2;font-family:Arial,Helvetica,sans-serif;\">"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f7f5f2;padding:24px 0;\">"
                + "<tr><td align=\"center\">"
                + "<table role=\"presentation\" width=\"100%\" style=\"max-width:480px;background-color:#ffffff;border-radius:12px;overflow:hidden;\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr><td style=\"background-color:#eb4d4b;padding:20px 24px;\">"
                + "<span style=\"color:#ffffff;font-size:20px;font-weight:bold;\">Routopia</span>"
                + "</td></tr>"
                + "<tr><td style=\"padding:24px;\">"
                + "<h1 style=\"font-size:20px;margin:0 0 8px;color:#1a1a1a;\">¡Reserva confirmada!</h1>"
                + "<p style=\"color:#555;margin:0 0 20px;\">Hola " + escape(userName) + ", tu reserva se realizó con éxito. Estos son los detalles:</p>"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top:1px solid #eee;\">"
                + row("Producto", escape(destinoName))
                + row("Fecha de uso", useDate)
                + row("Reservado el", bookedAt)
                + row("Personas", String.valueOf(booking.getPersonCount()))
                + notesRow
                + contactRow
                + "</table>"
                + "<p style=\"color:#999;font-size:12px;margin-top:24px;\">Número de reserva #" + booking.getId() + "</p>"
                + "</td></tr>"
                + "<tr><td style=\"background-color:#f7f5f2;padding:16px 24px;text-align:center;color:#999;font-size:12px;\">"
                + "© Routopia. Todos los derechos reservados."
                + "</td></tr>"
                + "</table>"
                + "</td></tr>"
                + "</table>"
                + "</body></html>";
    }

    private String row(String label, String value) {
        return "<tr>"
                + "<td style=\"padding:8px 0;color:#666;border-bottom:1px solid #f0f0f0;\">" + label + "</td>"
                + "<td style=\"padding:8px 0;font-weight:600;text-align:right;border-bottom:1px solid #f0f0f0;\">" + value + "</td>"
                + "</tr>";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
