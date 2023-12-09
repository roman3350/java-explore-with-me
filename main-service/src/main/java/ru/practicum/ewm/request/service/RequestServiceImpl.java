package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.exception.EventNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Status;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.ParticipationRequestMapper;
import ru.practicum.ewm.request.exception.*;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.exception.UserNotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    /**
     * Вывод запросов пользователя
     *
     * @param userId ID пользователя
     * @return Запросы
     */
    @Override
    public List<ParticipationRequestDto> getRequestCurrentUser(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    /**
     * Создание запроса
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Созданный запрос
     */
    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(eventId));
        if (requestRepository.findFirstByRequesterIdAndEventId(userId, eventId) != null) {
            throw new UserSentRequestCurrentEventException();
        }
        if (event.getInitiator().equals(user)) {
            throw new UserInitiatorEventException();
        }
        if (!event.getState().equals(Status.PUBLISHED)) {
            throw new EventNotPublishedException();
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new NoEmptyPlaceEventException();
        }
        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        Status status;
        if (event.getParticipantLimit() == 0) {
            status = Status.CONFIRMED;
        } else {
            status = Status.PENDING;
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository
                .save(ParticipationRequest.builder()
                        .event(event)
                        .requester(user)
                        .status(status)
                        .created(LocalDateTime.now())
                        .build()));
    }

    /**
     * Отмена запроса
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Отмененный запрос
     */
    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        ParticipationRequest participationRequest = requestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFound(requestId));
        participationRequest.setStatus(Status.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }
}
