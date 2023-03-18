package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIdIn(List<Long> list);

    List<Event> findEventsByCategory(Category category);

    List<Event> findEventsByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
            List<User> userList, List<State> stateList,
            List<Category> categoryList, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> findAllByAnnotationContainingOrDescriptionContainingAndCategoryInAndPaidAndEventDateBetween(
            String s, String s2, List<Category> categoryList, Boolean paid, LocalDateTime start, LocalDateTime end);
}

