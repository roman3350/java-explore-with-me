package ru.practicum.ewm.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.category.exception.CategoryNotFoundException;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.exception.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.exception.UserNotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String APP = "ewm-main-service";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsClient statsClient;

    /**
     * Вывод событий определенного пользователя
     *
     * @param userId ID пользователя
     * @param from   с какого элемента начинать
     * @param size   количество элементов на странице
     * @return События
     */
    @Override
    public List<EventShortDto> getEventsAddedByCurrentUser(long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return EventMapper.toEventShortDto(eventRepository.findAllByInitiatorId(userId, pageRequest));
    }

    /**
     * Создание события
     *
     * @param userId      ID пользователя создавшего событие
     * @param newEventDto Создаваемое событие
     * @return Созданное событие
     */
    @Override
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(()
                -> new CategoryNotFoundException(newEventDto.getCategory()));
        LocalDateTime eventTime = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);
        if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectEventDateException();
        }
        newEventDto.setEventDate(newEventDto.getEventDate().replace('T', ' ').substring(0, 19));
        Location location = locationRepository.save(newEventDto.getLocation());
        return EventMapper.toEventFullDto(eventRepository
                .save(EventMapper.newEventDtoToEvent(user, category, location, newEventDto)));
    }

    /**
     * Вывод событий по ID определенного пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Событие
     */
    @Override
    public EventFullDto getEventByEventIdAddedByCurrentUser(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        return EventMapper.toEventFullDto(eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(eventId)));
    }

    /**
     * Изменение события конкретного пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID событие
     * @param request Данные на обновление
     * @return Обновленное событие
     */
    @Override
    public EventFullDto patchEventByCurrentUser(long userId, long eventId, UpdateEventUserRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getInitiator().getId() != userId) {
            throw new UserNotInitiatorException();
        }
        if (event.getState().equals(Status.PUBLISHED)) {
            throw new ChangePublishedEventException();
        }
        if (request.getEventDate() != null) {
            LocalDateTime eventTime = LocalDateTime.parse(request.getEventDate(), FORMATTER);
            if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new IncorrectEventDateException();
            }
            event.setEventDate(eventTime);
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow(()
                    -> new CategoryNotFoundException(request.getCategory()));
            event.setCategory(category);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), FORMATTER));
        }
        if (request.getLocation() != null) {
            Location location = request.getLocation();
            if (locationRepository.findFirst1ByLatIsAndLonIs(location.getLat(), location.getLon()).isEmpty()) {
                locationRepository.save(location);
            }
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            if (event.getConfirmedRequests() > request.getParticipantLimit()) {
                throw new IncorrectParticipantLimitException();
            }
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            try {
                StateAction stateAction = StateAction.valueOf(request.getStateAction());
                if (stateAction.equals(StateAction.SEND_TO_REVIEW)) {
                    event.setState(Status.PENDING);
                } else {
                    event.setState(Status.CANCELED);
                }
            } catch (IllegalArgumentException e) {
                throw new IncorrectStateActionException();
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Получение информации о запросах на участие в событие пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return События
     */
    @Override
    public List<ParticipationRequestDto> getRequestEventCurrentUser(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getInitiator().getId() != userId) {
            throw new UserNotInitiatorException();
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.findAllByEventId(eventId));
    }

    /**
     * Изменение статуса заявок на участие в событии пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @param request Запрос на изменение
     * @return Результат запроса
     */
    @Override
    public EventRequestStatusUpdateResult patchStatusRequestCurrentUser(long userId,
                                                                        long eventId,
                                                                        EventRequestStatusUpdateRequest request) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(eventId));
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ParticipantLimitReachedException();
        }
        if (event.getInitiator().getId() != userId) {
            throw new UserNotInitiatorException();
        }
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(request.getRequestIds());
        List<ParticipationRequest> confirmed = new ArrayList<>();
        List<ParticipationRequest> rejected = new ArrayList<>();
        try {
            if (Status.valueOf(request.getStatus()).equals(Status.REJECTED)) {
                for (ParticipationRequest participationRequest : requests) {
                    if (!participationRequest.getStatus().equals(Status.PENDING)) {
                        throw new StatusNotPendingException();
                    }
                    rejected.add(participationRequest);
                    participationRequest.setStatus(Status.REJECTED);
                }
            } else if (Status.valueOf(request.getStatus()).equals(Status.CONFIRMED)) {
                for (ParticipationRequest participationRequest : requests) {
                    if (!participationRequest.getStatus().equals(Status.PENDING)) {
                        throw new StatusNotPendingException();
                    }
                    if (event.getConfirmedRequests() < event.getParticipantLimit() ||
                            event.getParticipantLimit() == 0) {
                        confirmed.add(participationRequest);
                        participationRequest.setStatus(Status.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        rejected.add(participationRequest);
                        participationRequest.setStatus(Status.REJECTED);
                    }
                }
            } else {
                throw new StatusNotConfirmedOrRejectedException();
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectStateActionException();
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        return new EventRequestStatusUpdateResult(ParticipationRequestMapper.toParticipationRequestDto(confirmed),
                ParticipationRequestMapper.toParticipationRequestDto(rejected));
    }

    /**
     * Поиск событий админом по критериям
     *
     * @param searchAdmin Критерии
     * @return События
     */
    @Override
    public List<EventFullDto> getEventsAdmin(SearchAdmin searchAdmin) {
        List<Long> users = searchAdmin.getUsers();
        List<Status> states = null;
        List<Long> categories = searchAdmin.getCategories();
        LocalDateTime rangeStart = null;
        LocalDateTime rangeEnd = null;
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (users != null) {
            if (!users.isEmpty()) {
                conditions.add(event.initiator.id.in(users));
            }
        }
        if (searchAdmin.getStates() != null) {
            states = searchAdmin.getStates().stream()
                    .map(Status::valueOf)
                    .collect(Collectors.toList());
            conditions.add(event.state.in(states));
        }
        if (categories != null) {
            if (!categories.isEmpty()) {
                conditions.add(event.category.id.in(categories));
            }
        }
        if (searchAdmin.getRangeStart() != null) {
            rangeStart = LocalDateTime.parse(searchAdmin.getRangeStart(), FORMATTER);
            conditions.add(event.eventDate.after(rangeStart));
        }
        if (searchAdmin.getRangeEnd() != null) {
            rangeEnd = LocalDateTime.parse(searchAdmin.getRangeEnd(), FORMATTER);
            conditions.add(event.eventDate.before(rangeEnd));
        }
        List<Event> findEvent;
        PageRequest pageRequest = PageRequest.of(searchAdmin.getFrom() / searchAdmin.getSize(),
                searchAdmin.getSize());
        if (conditions.size() == 0) {
            findEvent = eventRepository.findAll(pageRequest).getContent();
        } else {
            BooleanExpression finalCondition = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();
            findEvent = eventRepository.findAll(finalCondition, pageRequest).getContent();
        }
        return EventMapper.toEventFullDto(findEvent);
    }

    /**
     * Изменение события админом
     *
     * @param eventId ID события
     * @param request Данные на обновление
     * @return Обновленное событие
     */
    @Override
    public EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(request.getEventDate(), FORMATTER);
            if (eventDate.plusHours(1).isBefore(LocalDateTime.now())) {
                throw new IncorrectEventDateException();
            }
            event.setEventDate(eventDate);
        }
        if (request.getLocation() != null) {
            Location location = request.getLocation();
            if (locationRepository.findFirst1ByLatIsAndLonIs(location.getLat(), location.getLon()).isEmpty()) {
                locationRepository.save(location);
            }
            event.setLocation(location);
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (event.getState().equals(Status.PUBLISHED) || event.getState().equals(Status.CANCELED)) {
                throw new IncorrectStateActionException();
            }
            if (request.getStateAction().equals(StateAction.REJECT_EVENT.name())) {
                event.setState(Status.CANCELED);
            } else {
                event.setState(Status.PUBLISHED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    /**
     * Поиск событий по критериям
     *
     * @param searchUser Критерии
     * @param request    HTTP-запрос
     * @return События
     */
    @Override
    public List<EventShortDto> getEventUser(SearchUser searchUser, HttpServletRequest request) {
        statsClient.postEndPointHits(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        String text = searchUser.getText();
        List<Long> categories = searchUser.getCategories();
        Boolean paid = searchUser.getPaid();
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd = null;
        if (searchUser.getRangeStart() != null) {
            rangeStart = LocalDateTime.parse(searchUser.getRangeStart(), FORMATTER);
        } else {
            rangeStart = LocalDateTime.now();
        }
        if (searchUser.getRangeEnd() != null) {
            rangeEnd = LocalDateTime.parse(searchUser.getRangeEnd(), FORMATTER);
        }
        Boolean onlyAvailable = searchUser.getOnlyAvailable();
        String sort = searchUser.getSort();
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(event.eventDate.after(rangeStart));
        if (text != null) {
            conditions.add(event.annotation.likeIgnoreCase(text));
        }
        if (categories != null) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (rangeEnd != null) {
            if (rangeEnd.isAfter(rangeStart)) {
                conditions.add(event.eventDate.before(rangeEnd));
            } else {
                throw new IncorrectEventDateException();
            }

        }
        if (onlyAvailable != null) {
            conditions.add(event.participantLimit.gt(event.confirmedRequests));
        }
        Sort sortDB;
        if (sort.equals("EVENT_DATE")) {
            sortDB = Sort.by("eventDate").ascending();
        } else {
            sortDB = Sort.by("views").ascending();
        }
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        PageRequest pageRequest = PageRequest
                .of(searchUser.getFrom() > 0 ? searchUser.getFrom() / searchUser.getSize() : 0,
                        searchUser.getSize(), sortDB);
        return EventMapper.toEventShortDto(eventRepository.findAll(finalCondition, pageRequest).getContent());
    }

    /**
     * Вывод события по ID
     *
     * @param id      ID события
     * @param request HTTP-запрос
     * @return Событие
     */
    @Override
    public EventFullDto getEventId(long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, Status.PUBLISHED).orElseThrow(() ->
                new EventNotFoundException(id));
        event.setViews(1);
        statsClient.postEndPointHits(APP, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }
}