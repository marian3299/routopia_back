package com.back.routopia.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingHistoryDTO {
    private Long id;
    private Long destinoId;
    private String destinoName;
    private String destinoImageUrl;
    private LocalDate bookingDate;
    private Integer personCount;
    private String notes;
    private String status;
    private LocalDateTime createdAt;

    public BookingHistoryDTO(Long id, Long destinoId, String destinoName, String destinoImageUrl, LocalDate bookingDate, Integer personCount, String notes, String status, LocalDateTime createdAt) {
        this.id = id;
        this.destinoId = destinoId;
        this.destinoName = destinoName;
        this.destinoImageUrl = destinoImageUrl;
        this.bookingDate = bookingDate;
        this.personCount = personCount;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public String getDestinoName() {
        return destinoName;
    }

    public String getDestinoImageUrl() {
        return destinoImageUrl;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
