package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(long id);

    ParticipationRequest findFirstByRequesterIdAndEventId(long userId, long eventId);

    List<ParticipationRequest> findAllByEventId(long id);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);
}
