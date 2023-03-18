package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.request.dto.RequestConfirmedDtoOut;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    Request findByEventIdAndRequesterId(Long eventId, Long userId);

    List<Request> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    List<Request> findAllByEventIdAndEventInitiatorIdAndIdIn(Long eventId, Long userId, List<Long> ids);

    Long countParticipationByEventIdAndStatus(Long eventId, RequestStatus status);

    Long countRequestByEventIdAndStatus(Long eventId, RequestStatus status);

    @Query("SELECT new ru.practicum.mainservice.request.dto.RequestConfirmedDtoOut(r.event.id, COUNT(r.id)) " +
           "FROM Request r " +
           "WHERE r.event.id IN :eventIds " +
           "AND r.status = 'CONFIRMED' " +
           "GROUP BY r.event.id")
    List<RequestConfirmedDtoOut> countConfirmedRequestsByEventIds(@Param("eventIds") List<Long> eventIds);
}