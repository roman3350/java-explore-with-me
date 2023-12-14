package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SearchAdmin;
import ru.practicum.ewm.event.model.SearchUser;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventController {
    private final EventService eventService;

    /**
     * Вывод событий определенного пользователя
     *
     * @param userId ID пользователя
     * @param from   с какого элемента начинать
     * @param size   количество элементов на странице
     * @return События
     */
    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsAddedByCurrentUser(@PathVariable long userId,
                                                           @PositiveOrZero
                                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                                           @Positive
                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Вывод событий определенного пользователя с ID={}, from={}, size={}", userId, from, size);
        return eventService.getEventsAddedByCurrentUser(userId, from, size);
    }

    /**
     * Создание события
     *
     * @param userId      ID пользователя создавшего событие
     * @param newEventDto Создаваемое событие
     * @return Созданное событие
     */
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Создание события пользователем с ID={}, событие={}", userId, newEventDto);
        return eventService.postEvent(userId, newEventDto);
    }

    /**
     * Вывод событий по ID определенного пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Событие
     */
    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByEventIdAddedByCurrentUser(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Вывод событий по ID={} определенного пользователя с ID={}", eventId, userId);
        return eventService.getEventByEventIdAddedByCurrentUser(userId, eventId);
    }

    /**
     * Изменение события конкретного пользователя
     *
     * @param userId                 ID пользователя
     * @param eventId                ID событие
     * @param updateEventUserRequest Данные на обновление
     * @return Обновленное событие
     */
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto patchEventByCurrentUser(@PathVariable long userId,
                                                @PathVariable long eventId,
                                                @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Изменение события с ID={}, пользователя с ID={}, данные на обновление {}",
                eventId, userId, updateEventUserRequest);
        return eventService.patchEventByCurrentUser(userId, eventId, updateEventUserRequest);
    }

    /**
     * Получение информации о запросах на участие в событие пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return События
     */
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestEventCurrentUser(@PathVariable long userId,
                                                                    @PathVariable long eventId) {
        log.info("Получение информации о запросах на участие в событие с ID={}, пользователя с ID={}", eventId, userId);
        return eventService.getRequestEventCurrentUser(userId, eventId);
    }

    /**
     * Изменение статуса заявок на участие в событии пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @param request Запрос на изменение
     * @return Результат запроса
     */
    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchStatusRequestCurrentUser(@PathVariable long userId,
                                                                        @PathVariable long eventId,
                                                                        @RequestBody
                                                                        EventRequestStatusUpdateRequest request) {
        log.info("Изменение статуса заявок на участие в событии с ID={}, пользователя с ID={}, Запрос {} ",
                eventId, userId, request);
        return eventService.patchStatusRequestCurrentUser(userId, eventId, request);
    }

    /**
     * Добавление комментария пользователем
     *
     * @param userId  ID пользователя
     * @param eventId ID комментария
     * @param comment комментарий
     * @return Добавленный комментарий
     */
    @PostMapping("/user/{userId}/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable long userId,
                                  @PathVariable long eventId,
                                  @Valid @RequestBody NewCommentDto comment) {
        log.info("Добавление комментария пользователем с ID={}, событию с ID={}, комментарий {}",
                userId, eventId, comment);
        return eventService.postComment(userId, eventId, comment);
    }

    /**
     * Изменение комментария
     *
     * @param userId    ID пользователя
     * @param commentId ID комментария
     * @param comment   Данные на изменения
     * @return Изменённый комментарий
     */
    @PatchMapping("/user/{userId}/comment/{commentId}")
    public CommentDto patchComment(@PathVariable long userId,
                                   @PathVariable long commentId,
                                   @Valid @RequestBody NewCommentDto comment) {
        log.info("Изменение комментария с ID={}, пользователем с ID={}, комментарий {}", commentId, userId, comment);
        return eventService.patchComment(userId, commentId, comment);
    }

    /**
     * Удаление комментария
     *
     * @param userId    ID пользователя
     * @param commentId ID комментария
     */
    @DeleteMapping("/user/{userId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long commentId) {
        log.info("Удаление комментария с ID={}, пользователем с ID={}", commentId, userId);
        eventService.deleteComment(userId, commentId);
    }

    /**
     * Удаление комментария админом
     *
     * @param commentId ID комментария
     */
    @DeleteMapping("/admin/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable long commentId) {
        log.info("Удаление комментария с ID={}", commentId);
        eventService.deleteCommentByAdmin(commentId);
    }

    /**
     * Поиск событий админом по критериям
     *
     * @param users      Пользователи
     * @param states     Статусы
     * @param categories Категории
     * @param rangeStart Начало диапазона
     * @param rangeEnd   Конец диапазона
     * @param from       с какого элемента выводить
     * @param size       количество элементов на странице
     * @return События
     */
    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsAdmin(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<String> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Поиск событий админом с критериями: users={}, states={}, categories={}, rangeStart={}, " +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsAdmin(
                new SearchAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    /**
     * Изменение события админом
     *
     * @param eventId                 ID события
     * @param updateEventAdminRequest Данные на обновление
     * @return Обновленное событие
     */
    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto patchEventAdmin(@PathVariable long eventId,
                                        @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Обновление данных события админом с ID={}, Данные на обновление {}",
                eventId, updateEventAdminRequest);
        return eventService.patchEventAdmin(eventId, updateEventAdminRequest);
    }

    /**
     * Вывод событий по критериям
     *
     * @param text          Описание
     * @param categories    Категории
     * @param paid          Платное или бесплатное
     * @param rangeStart    Начало диапазона поиска
     * @param rangeEnd      Конец диапазона поиска
     * @param onlyAvailable Доступные или нет
     * @param sort          Сортировка
     * @param from          с какого элемента выводить
     * @param size          количество элементов на странице
     * @param request       HTTP-запрос
     * @return События
     */
    @GetMapping("/events")
    public List<EventShortDto> getEventUser(@RequestParam(name = "text", required = false)
                                            String text,
                                            @RequestParam(name = "categories", required = false)
                                            List<Long> categories,
                                            @RequestParam(name = "paid", required = false)
                                            Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false)
                                            String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false)
                                            String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                            Boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "VIEWS")
                                            String sort,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                            int from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10")
                                            int size,
                                            HttpServletRequest request) {
        log.info("Получение событий с по критериям text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}," +
                        " onlyAvailable={}, sort={}, from={}, size={}", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEventUser(new SearchUser(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size), request);
    }

    /**
     * Вывод события по ID
     *
     * @param eventId ID события
     * @param request HTTP-запрос
     * @return Событие
     */
    @GetMapping("/events/{eventId}")
    public EventFullDto getEventId(@PathVariable long eventId,
                                   HttpServletRequest request) {
        log.info("Получение информации о событии с ID={}", eventId);
        return eventService.getEventId(eventId, request);
    }

    /**
     * Вывод комментариев события
     *
     * @param eventId ID события
     * @return Комментарии события
     */
    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getComments(@PathVariable long eventId) {
        log.info("Вывод комментариев события с ID={}", eventId);
        return eventService.getComments(eventId);
    }
}
