package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ContentDetectedException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.request.dto.RequestPatchDtoIn;
import ru.practicum.mainservice.request.dto.RequestPatchDtoOut;
import ru.practicum.mainservice.request.dto.RequestDtoOut;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.RequestStatus;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RequestDtoOut add(Long userId, Long eventId) {
        Event event = findByEventId(eventId);

        Request request = new Request();

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ContentDetectedException("RequestServiceImpl: initiator can't add request.");
        }
        User user = userRepository.findById(userId).get();

        if (Objects.nonNull(requestRepository.findByEventIdAndRequesterId(eventId, userId))) {
            throw new ContentDetectedException("RequestServiceImpl: request already exists.");
        }
        request.setRequester(user);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ContentDetectedException("RequestServiceImpl: event not published.");
        }
        request.setEvent(event);

        if (request.getEvent().getParticipantLimit()
                <= requestRepository.countRequestByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)) {
            throw new ContentDetectedException("RequestServiceImpl: event entry limit.");
        }

        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        request.setCreated(LocalDateTime.now().withNano(0));
        log.debug("RequestServiceImpl: add request with userId= {} and eventId= {}", userId, eventId);
        requestRepository.saveAndFlush(request);
        return requestMapper.toRequestDtoOut(request);
    }

    @Override
    public List<RequestDtoOut> getById(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(requestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDtoOut> getByEventId(Long userId, Long eventId) {
        return requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .stream()
                .map(requestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestPatchDtoOut update(Long userId, Long eventId, RequestPatchDtoIn updateRequest) {
        Event event = findByEventId(eventId);

        Long participantLimit = event.getParticipantLimit();
        Long confirmedCount = requestRepository.countRequestByEventIdAndStatus(eventId,
                RequestStatus.CONFIRMED);

        if (participantLimit <= confirmedCount) {
            throw new ContentDetectedException("RequestServiceImpl: event entry limit.");
        }

        List<Long> requestIds = updateRequest.getRequestIds();
        List<Request> requests = requestRepository.findAllByEventIdAndEventInitiatorIdAndIdIn(eventId,
                userId, requestIds);

        if (participantLimit < requests.size() + confirmedCount) {
            throw new ContentDetectedException("RequestServiceImpl: event entry limit.");
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        if (updateRequest.getStatus().equals("CONFIRMED")) {
            confirmedRequests = confirmRequests(requests, participantLimit, confirmedCount);
        } else if (updateRequest.getStatus().equals("REJECTED")) {
            rejectedRequests = rejectRequests(requests);
        }

        requestRepository.saveAll(requests);

        List<RequestDtoOut> confirmedRequestDtos = confirmedRequests.stream()
                .map(requestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
        List<RequestDtoOut> rejectedRequestDtos = rejectedRequests.stream()
                .map(requestMapper::toRequestDtoOut)
                .collect(Collectors.toList());

        return RequestPatchDtoOut.builder()
                .confirmedRequests(confirmedRequestDtos)
                .rejectedRequests(rejectedRequestDtos)
                .build();
    }

    @Override
    @Transactional
    public RequestDtoOut delete(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("RequestServiceImpl: request with id=" +
                                requestId + " was not found."));
        request.setStatus(RequestStatus.CANCELED);
        log.debug("RequestServiceImpl: delete request with userId= {} and requestId= {}", userId, requestId);
        return requestMapper.toRequestDtoOut(request);
    }

    private Event findByEventId(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("RequestServiceImpl: event with id=" +
                                id + " was not found."));
    }

    private List<Request> confirmRequests(List<Request> requests, Long participantLimit, Long confirmedCount) {
        List<Request> confirmedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (participantLimit > confirmedCount) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                confirmedCount++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }
        return confirmedRequests;
    }

    private List<Request> rejectRequests(List<Request> requests) {
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            request.setStatus(RequestStatus.REJECTED);
            rejectedRequests.add(request);
        }
        return rejectedRequests;
    }
}
