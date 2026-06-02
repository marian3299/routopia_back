package com.back.routopia.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private Long destinoId;
    private Long userId;
    private LocalDate bookingDate;
    private Integer personCount;
    private String status;
    private LocalDateTime createdAt;

    public BookingDTO(Long id, Long destinoId, Long userId, LocalDate bookingDate, Integer personCount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.destinoId = destinoId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.personCount = personCount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
