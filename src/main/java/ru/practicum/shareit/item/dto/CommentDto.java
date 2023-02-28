package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.user.*;

@Data
public class CommentDto {

    private Integer id;

    @NotBlank(message = "содержание отзыва не может быть пустым")
    private String text;

    @NotBlank(message = "id вещи не может быть пустым")
    private Item item;

    @NotBlank(message = "id автора не может быть пустым")
    private User author;

    @NotBlank(message = "имя автора не может быть пустым")
    private String authorName;

    private LocalDateTime created;
}
