package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import ru.practicum.shareit.user.User;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    private Integer request;
}
