package com.back.routopia.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingAvailabilityDTO {
    private Long destinoId;
    private LocalDate from;
    private LocalDate to;
    private List<LocalDate> blockedDates;

    public BookingAvailabilityDTO(Long destinoId, LocalDate from, LocalDate to, List<LocalDate> blockedDates) {
        this.destinoId = destinoId;
        this.from = from;
        this.to = to;
        this.blockedDates = blockedDates;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public List<LocalDate> getBlockedDates() {
        return blockedDates;
    }
}
