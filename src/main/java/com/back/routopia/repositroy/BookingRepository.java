package com.back.routopia.repositroy;

import com.back.routopia.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByDestinoIdAndBookingDate(Long destinoId, LocalDate bookingDate);

    List<Booking> findAllByDestinoIdAndBookingDateBetweenOrderByBookingDateAsc(
            Long destinoId,
            LocalDate from,
            LocalDate to
    );

    boolean existsByUserIdAndDestinoIdAndStatusAndBookingDateLessThanEqual(
            Long userId,
            Long destinoId,
            String status,
            LocalDate bookingDate
    );
}
