package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StatusDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final UserService userService;

    private final ItemService itemService;

    private final BookingMapper bookingMapper;

    private void checkValidUser(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private void checkOnValidBeforeAdd(Booking booking, Integer ownerId) {
        if (booking.getItem() == null || booking.getBooker() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (booking.getStart() == null || booking.getEnd() == null

                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    private Item findItem(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public BookingDto add(BookingDto bookingDto, Integer userId) {
        Booking booking = bookingMapper.toBooking(bookingDto);
        checkOnValidBeforeAdd(booking, userId);
        booking.setStatus(Status.WAITING);
        log.info("добавлено бронирование /{}/", booking);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto updApprove(Integer bookingId, Boolean approved, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("updApprove>>>>>booking={}, userId={}", booking.toString(), userId);
        if (booking.getStatus() == Status.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            log.info("updApprove>>NotFoundItem>>>");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        log.info("изменен статус бронирования /{}/", booking);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto findById(Integer bookingId, Integer userId) {
        checkValidUser(userId);
        Booking booking = bookingRepository.getById(bookingId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("запрошено бронирование /{}/ владельца /{}/", bookingId, userId);
        return bookingMapper.toDto(booking);
    }

    private Collection<Booking> findAllByUserAndStatus(Integer userId, Status stat) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ со статусом /{}/", userId, stat);
        return bookingRepository.getBookingsByUserAndStatus(userId, stat);
    }

    @Override
    public Collection<BookingDto> findAllByUser(Integer userId, StatusDto state) {
        checkValidUser(userId);
        ArrayList<BookingDto> listDto = new ArrayList<>();
        switch (state) {
            case ALL:
                for (Booking booking : bookingRepository.getBookingsByUser(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                log.info("запрошены бронирования пользователя /{}/", userId);
                break;
            case WAITING:
            case APPROVED:
            case REJECTED:
            case CANCELED:
                for (Booking booking : findAllByUserAndStatus(userId, bookingMapper.toStatus(state))) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case FUTURE:
                for (Booking booking : findAllByUserFuture(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case PAST:
                for (Booking booking : findAllByUserPast(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case CURRENT:
                for (Booking booking : findAllByUserCurrent(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
        }

        return listDto;
    }


    private Collection<Booking> findAllByUserFuture(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=FUTURE", userId);
        return bookingRepository.findByBookerIdAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByUserPast(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=PAST", userId);
        return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByUserCurrent(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=CURRENT", userId);
        return bookingRepository.getByUserCurrent(userId, LocalDateTime.now());
    }

    @Override
    public Collection<BookingDto> findAllByOwner(Integer userId, StatusDto state) {
        checkValidUser(userId);
        ArrayList<BookingDto> listDto = new ArrayList<>();
        switch (state) {
            case ALL:
                for (Booking booking : bookingRepository.getAllByOwner(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                log.info("запрошены бронирования вещей владельца /{}/", userId);
                break;
            case WAITING:
            case APPROVED:
            case REJECTED:
            case CANCELED:
                Status status = bookingMapper.toStatus(state);
                for (Booking booking : findAllByOwnerAndStatus(userId, status)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case FUTURE:
                for (Booking booking : findAllByOwnerFuture(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case PAST:
                for (Booking booking : findAllByOwnerPast(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
                break;
            case CURRENT:
                for (Booking booking : findAllByOwnerCurrent(userId)) {

                    listDto.add(bookingMapper.toDto(booking));
                }
        }
        return listDto;
    }

    private Collection<Booking> findAllByOwnerAndStatus(Integer userId, Status stat) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ со статусом /{}/", userId, stat);
        return bookingRepository.getAllByOwnerAndStatus(userId, stat);
    }

    private Collection<Booking> findAllByOwnerFuture(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=FUTURE", userId);
        return bookingRepository.getByOwnerFuture(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByOwnerPast(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=PAST", userId);
        return bookingRepository.getByOwnerPast(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByOwnerCurrent(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=CURRENT", userId);
        return bookingRepository.getByOwnerCurrent(userId, LocalDateTime.now());
    }
}
