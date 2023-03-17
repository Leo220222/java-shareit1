package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
public class CommentMapper {

    private UserRepository userRepository;
    private ItemRepository itemRepository;

    public CommentDto toDto(Comment comment, User user) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem().getId());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorName(user.getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(itemRepository.findById(commentDto.getItem()).orElseThrow());
        comment.setAuthor(userRepository.findById(commentDto.getAuthor()).orElseThrow());
        return comment;
    }
}
