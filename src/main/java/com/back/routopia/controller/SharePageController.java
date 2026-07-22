package com.back.routopia.controller;

import com.back.routopia.entity.Destino;
import com.back.routopia.service.DestinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Página HTML mínima con Open Graph para crawlers de redes sociales.
 * Los usuarios humanos son redirigidos al frontend (/tour/:id).
 */
@RestController
@CrossOrigin(origins = "*")
public class SharePageController {

    @Autowired
    private DestinoService destinoService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    @GetMapping(value = "/share/destino/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> shareDestino(@PathVariable Long id) {
        Destino destino = destinoService.find_by_id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        String title = escapeHtml(destino.getName() != null ? destino.getName() : "Routopia");
        String description = escapeHtml(truncate(destino.getDescription(), 180));
        String image = escapeHtml(destino.getImageUrl() != null ? destino.getImageUrl() : "");
        String frontendProductUrl = frontendUrl.replaceAll("/$", "") + "/tour/" + id;
        String absoluteShareUrl = escapeHtml(backendUrl.replaceAll("/$", "") + "/share/destino/" + id);
        String canonicalFrontend = escapeHtml(frontendProductUrl);

        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                  <meta charset="utf-8" />
                  <title>%s | Routopia</title>
                  <meta name="description" content="%s" />
                  <meta property="og:type" content="website" />
                  <meta property="og:site_name" content="Routopia" />
                  <meta property="og:title" content="%s" />
                  <meta property="og:description" content="%s" />
                  <meta property="og:image" content="%s" />
                  <meta property="og:url" content="%s" />
                  <meta name="twitter:card" content="summary_large_image" />
                  <meta name="twitter:title" content="%s" />
                  <meta name="twitter:description" content="%s" />
                  <meta name="twitter:image" content="%s" />
                  <meta http-equiv="refresh" content="0;url=%s" />
                  <link rel="canonical" href="%s" />
                </head>
                <body>
                  <p>Redirigiendo a <a href="%s">%s</a>...</p>
                  <script>window.location.replace(%s);</script>
                </body>
                </html>
                """.formatted(
                title,
                description,
                title,
                description,
                image,
                absoluteShareUrl,
                title,
                description,
                image,
                canonicalFrontend,
                canonicalFrontend,
                canonicalFrontend,
                title,
                toJsString(frontendProductUrl)
        );

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    private static String truncate(String text, int max) {
        if (text == null || text.isBlank()) {
            return "Descubrí este destino en Routopia";
        }
        String clean = text.trim().replaceAll("\\s+", " ");
        if (clean.length() <= max) return clean;
        return clean.substring(0, max - 1).trim() + "…";
    }

    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String toJsString(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                + "\"";
    }
}
