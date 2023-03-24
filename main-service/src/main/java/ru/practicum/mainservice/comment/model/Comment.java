package ru.practicum.mainservice.comment.model;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "created")
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private CommentStatus state;
}