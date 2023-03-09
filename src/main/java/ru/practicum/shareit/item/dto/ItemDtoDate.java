package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;

import java.util.HashSet;
import java.util.Set;

import ru.practicum.shareit.user.*;

@Data
public class ItemDtoDate {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private Integer request;

    private Booking lastBooking;

    private Booking nextBooking;

    private Set<CommentDto> comments = new HashSet<>();
}
