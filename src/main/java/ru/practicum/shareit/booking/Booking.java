package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User bookerId;

    @Enumerated(EnumType.STRING)
    private Status status;
}
