package com.back.routopia.service;

import com.back.routopia.dto.BookingAvailabilityDTO;
import com.back.routopia.dto.BookingDTO;
import com.back.routopia.dto.BookingRequestDTO;
import com.back.routopia.entity.Booking;
import com.back.routopia.entity.Destino;
import com.back.routopia.entity.User;
import com.back.routopia.repositroy.BookingRepository;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DestinoRespository destinoRespository;

    @Autowired
    private UserRepository userRepository;

    public BookingDTO createBooking(BookingRequestDTO request) {
        if (request.getDestinoId() == null || request.getUserId() == null || request.getBookingDate() == null || request.getPersonCount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios para crear la reserva");
        }

        Long destinoId = request.getDestinoId();
        Long userId = request.getUserId();

        if (request.getPersonCount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad de personas debe ser mayor a cero");
        }

        if (bookingRepository.existsByDestinoIdAndBookingDate(destinoId, request.getBookingDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esa fecha ya está reservada para este destino");
        }

        Destino destino = destinoRespository.findById(Objects.requireNonNull(destinoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Booking booking = new Booking();
        booking.setDestino(destino);
        booking.setUser(user);
        booking.setBookingDate(request.getBookingDate());
        booking.setPersonCount(request.getPersonCount());
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking);
    }

    public BookingAvailabilityDTO getAvailability(Long destinoId, LocalDate from, LocalDate to) {
        if (destinoId == null || from == null || to == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destinoId, from y to son obligatorios");
        }
        if (from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango de fechas es inválido");
        }

        destinoRespository.findById(destinoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        List<LocalDate> blockedDates = bookingRepository
                .findAllByDestinoIdAndBookingDateBetweenOrderByBookingDateAsc(destinoId, from, to)
                .stream()
                .map(Booking::getBookingDate)
                .distinct()
                .collect(Collectors.toList());

        return new BookingAvailabilityDTO(destinoId, from, to, blockedDates);
    }

    private BookingDTO mapToDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getDestino().getId(),
                booking.getUser().getId(),
                booking.getBookingDate(),
                booking.getPersonCount(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
