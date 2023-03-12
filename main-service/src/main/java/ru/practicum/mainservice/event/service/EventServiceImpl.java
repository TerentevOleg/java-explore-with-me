package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.StateAction;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ContentDetectedException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.location.dto.LocationDtoOut;
import ru.practicum.mainservice.location.mapper.LocationMapper;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.location.repository.LocationRepository;
import ru.practicum.mainservice.request.model.RequestStatus;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.stats.dto.StatsDtoOut;
import ru.practicum.statsclient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final LocationMapper locationMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public EventDtoOut add(Long userId, EventDtoIn eventDtoIn) {
        if (LocalDateTime.parse(eventDtoIn.getEventDate(), formatter).isBefore(LocalDateTime.now().minusHours(2L))) {
            throw new ContentDetectedException("EventServiceImpl: invalid date, date" +
                    LocalDateTime.now() + " must be in the past.");
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        Event event = EventMapper.toEvent(eventDtoIn);
        event.setCategory(categoryRepository.findById(eventDtoIn.getCategory()).get());
        setConfirmedRequests(event);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(optionalUser.get());
        event.setLocation(addLocation(eventDtoIn.getLocation()));
        event.setState(State.PENDING);
        event.setViews(0L);
        log.debug("EventServiceImpl: add eventTitle= {}", eventDtoIn.getTitle());
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventDtoOut getById(Long id) {
        Event event = findEventById(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("EventServiceImpl: event with id=" + id + " was not found.");
        }
        setConfirmedRequests(event);
        String startDate = LocalDateTime.now().minusDays(10L).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        ResponseEntity<List> responseList = statsClient.get(startDate, endDate,
                Collections.singletonList("/events/" + id), false);
        List<StatsDtoOut> list = Optional.ofNullable(responseList.getBody()).orElse(Collections.emptyList());
        if (!list.isEmpty()) {
            event.setViews(list.get(0).getHits());
        }
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventDtoOut getById(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDtoOut> getByUserId(Long userId, Integer from, Integer size) {
        List<Event> list = eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from, size));
        return list.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoOut> getByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<User> userList = userRepository.findAllById(users);
        List<State> stateList = Optional.ofNullable(states)
                .orElse(Collections.emptyList())
                .stream()
                .map(State::valueOf)
                .collect(Collectors.toList());

        List<Category> categoryList = categoryRepository.findAllById(categories);
        LocalDateTime start = Optional.ofNullable(rangeStart)
                .map(str -> LocalDateTime.parse(str, formatter))
                .orElse(LocalDateTime.now().withNano(0));
        LocalDateTime end = Optional.ofNullable(rangeEnd)
                .map(str -> LocalDateTime.parse(str, formatter))
                .orElse(LocalDateTime.now().withNano(0).plusYears(1000L));
        List<Event> list = eventRepository.findEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
                userList, stateList, categoryList, start, end, PageRequest.of(from, size));

        return list.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDtoOut> getEventsByFilters(String text, List<Long> categories, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean available,
                                                     String sort, Integer from, Integer size) {
        List<Category> categoryList;
        if (Objects.nonNull(categories)) {
            categoryList = categoryRepository.findAllById(categories);
        } else {
            categoryList = categoryRepository.findAll();
        }
        LocalDateTime start;
        LocalDateTime end;
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            start = LocalDateTime.now().withNano(0);
            end = LocalDateTime.now().withNano(0).plusYears(1000L);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        List<Event> list = eventRepository
                .findAllByAnnotationContainingOrDescriptionContainingAndCategoryInAndPaidAndEventDateBetween(
                        text, text, categoryList, paid, start, end);
        list.forEach(this::setConfirmedRequests);
        List<Event> sortedList = new ArrayList<>();
        if (Objects.nonNull(sort)) {
            if (sort.equals("EVENT_DATE")) {
                sortedList = list.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            }
            if (sort.equals("VIEWS")) {
                sortedList = list.stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .collect(Collectors.toList());
            }
        }
        String startDate = LocalDateTime.now().minusDays(10L).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        statsClient.get(startDate, endDate, Collections.singletonList("/events"), false);

        return sortedList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDtoOut updateByUserId(Long userId, Long eventId, EventUserPatchDtoIn eventUserPatchDtoIn) {
        Event event = findEventById(eventId);
        if (event.getState().equals(State.CANCELED)
                || event.getState().equals(State.PENDING)) {

            if (Objects.nonNull(eventUserPatchDtoIn.getAnnotation())) {
                event.setAnnotation(eventUserPatchDtoIn.getAnnotation());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getCategory())) {
                event.setCategory(categoryRepository.findById(eventUserPatchDtoIn.getCategory()).get());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getDescription())) {
                event.setDescription(eventUserPatchDtoIn.getDescription());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getEventDate())) {
                event.setEventDate(LocalDateTime.parse(eventUserPatchDtoIn.getEventDate(), formatter));
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
                    throw new ContentDetectedException("EventServiceImpl: the event cannot " +
                            "be earlier than 2 hours before the start.");
                }
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getLocation())) {
                event.setLocation(locationMapper.toLocation(eventUserPatchDtoIn.getLocation()));
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getPaid())) {
                event.setPaid(eventUserPatchDtoIn.getPaid());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getParticipantLimit())) {
                event.setParticipantLimit(eventUserPatchDtoIn.getParticipantLimit());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getRequestModeration())) {
                event.setRequestModeration(eventUserPatchDtoIn.getRequestModeration());
            }
            if (Objects.nonNull(eventUserPatchDtoIn.getTitle())) {
                event.setTitle(eventUserPatchDtoIn.getTitle());
            }
            switch (eventUserPatchDtoIn.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
            log.debug("EventServiceImpl: update event with eventTitle= {}", eventUserPatchDtoIn.getTitle());
            eventRepository.save(event);
            return EventMapper.toEventFullDto(event);
        } else {
            throw new ContentDetectedException("EventServiceImpl: only pending or canceled events can be changed.");
        }
    }

    @Override
    @Transactional
    public EventDtoOut updateByAdmin(Long eventId, EventAdminPatchDtoIn eventAdminRequest) {
        Event event = findEventById(eventId);
        if (Objects.nonNull(eventAdminRequest.getEventDate())) {
            if (LocalDateTime.parse(eventAdminRequest.getEventDate(), formatter)
                    .isBefore(LocalDateTime.now().plusHours(1L))) {
                throw new ContentDetectedException("EventServiceImpl: event can't be earlier " +
                        "than one hours from the current moment");
            }
        }
        if (Objects.nonNull(eventAdminRequest.getAnnotation())) {
            event.setAnnotation(eventAdminRequest.getAnnotation());
        }
        if (Objects.nonNull(eventAdminRequest.getCategory())) {
            event.setCategory(categoryRepository.findById(eventAdminRequest.getCategory()).get());
        }
        if (Objects.nonNull(eventAdminRequest.getDescription())) {
            event.setDescription(eventAdminRequest.getDescription());
        }
        if (Objects.nonNull(eventAdminRequest.getEventDate())) {
            event.setEventDate(LocalDateTime.parse(eventAdminRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (Objects.nonNull(eventAdminRequest.getLocation())) {
            event.setLocation(addLocation(eventAdminRequest.getLocation()));
        }
        if (Objects.nonNull(eventAdminRequest.getPaid())) {
            event.setPaid(eventAdminRequest.getPaid());
        }
        if (Objects.nonNull(eventAdminRequest.getParticipantLimit())) {
            event.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }
        if (Objects.nonNull(eventAdminRequest.getRequestModeration())) {
            event.setRequestModeration(eventAdminRequest.getRequestModeration());
        }
        if (Objects.nonNull(eventAdminRequest.getTitle())) {
            event.setTitle(eventAdminRequest.getTitle());
        }

        if (eventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            if (event.getState().equals(State.PENDING)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new ContentDetectedException("EventServiceImpl: сan't post event because it's PENDING.");
            }
        }
        if (Objects.nonNull(eventAdminRequest.getStateAction())) {
            if (eventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(State.PUBLISHED)) {
                    throw new ContentDetectedException("EventServiceImpl: сan't post event because it's PUBLISHED.");
                } else {
                    event.setState(State.CANCELED);
                }
            }
        }
        log.debug("EventServiceImpl: update event with eventTitle= {}", eventAdminRequest.getTitle());
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    private Location addLocation(LocationDtoOut locationDtoOut) {
        return locationRepository.saveAndFlush(locationMapper.toLocation(locationDtoOut));
    }

    private void setConfirmedRequests(Event event) {
        event.setConfirmedRequests(requestRepository
                .countParticipationByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED));
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("EventServiceImpl: event with id=" +
                                id + " was not found."));
    }
}
