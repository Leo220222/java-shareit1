package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StatusDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Service
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStatus(bookingDto.getStatus());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(bookingDto.getItem());
        booking.setBooker(bookingDto.getBooker());
        return booking;
    }

    public BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setBooker(booking.getBooker());
        dto.setUserId(booking.getBooker().getId());
        dto.setItem(booking.getItem());
        dto.setItemId(booking.getItem().getId());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public Status toStatus(StatusDto statusDto) {
        Status status = null;
        switch (statusDto) {
            case WAITING:
                status = Status.WAITING;
                break;
            case APPROVED:
                status = Status.APPROVED;
                break;
            case REJECTED:
                status = Status.REJECTED;
                break;
            case CANCELED:
                status = Status.CANCELED;
        }
        return status;
    }
}
