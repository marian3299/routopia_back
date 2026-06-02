package com.back.routopia.controller;

import com.back.routopia.dto.BookingAvailabilityDTO;
import com.back.routopia.dto.BookingDTO;
import com.back.routopia.dto.BookingRequestDTO;
import com.back.routopia.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Bookings", description = "Creación de reservas y consulta de fechas bloqueadas")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Crear una reserva (si la fecha está libre)")
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingRequestDTO request) {
        BookingDTO createdBooking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @Operation(summary = "Obtener fechas bloqueadas por destino en un rango")
    @GetMapping("/availability")
    public ResponseEntity<BookingAvailabilityDTO> getAvailability(
            @RequestParam Long destinoId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        BookingAvailabilityDTO availability = bookingService.getAvailability(destinoId, from, to);
        return ResponseEntity.ok(availability);
    }
}
