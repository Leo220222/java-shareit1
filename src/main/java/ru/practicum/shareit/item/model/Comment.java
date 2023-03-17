package ru.practicum.shareit.item.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import ru.practicum.shareit.user.User;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    private LocalDateTime created = LocalDateTime.now();
}
