package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;

import ru.practicum.shareit.user.*;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    private Boolean available;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    @Column(name = "request")
    private Integer request;
}
