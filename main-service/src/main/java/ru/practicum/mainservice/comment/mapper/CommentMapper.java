package ru.practicum.mainservice.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentDtoOut toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    Comment toComment(CommentDtoOut commentDtoOut);
}
