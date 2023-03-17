package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;


import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;

    private Integer userId;

    private Status status;
}
