package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SearchAdmin;
import ru.practicum.ewm.event.model.SearchUser;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    /**
     * Вывод событий определенного пользователя
     *
     * @param userId ID пользователя
     * @param from   с какого элемента начинать
     * @param size   количество элементов на странице
     * @return События
     */
    List<EventShortDto> getEventsAddedByCurrentUser(long userId, int from, int size);

    /**
     * Создание события
     *
     * @param userId      ID пользователя создавшего событие
     * @param newEventDto Создаваемое событие
     * @return Созданное событие
     */
    EventFullDto postEvent(long userId, NewEventDto newEventDto);

    /**
     * Вывод событий по ID определенного пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Событие
     */
    EventFullDto getEventByEventIdAddedByCurrentUser(long userId, long eventId);

    /**
     * Изменение события конкретного пользователя
     *
     * @param userId                 ID пользователя
     * @param eventId                ID событие
     * @param updateEventUserRequest Данные на обновление
     * @return Обновленное событие
     */
    EventFullDto patchEventByCurrentUser(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * Получение информации о запросах на участие в событие пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return События
     */
    List<ParticipationRequestDto> getRequestEventCurrentUser(long userId, long eventId);

    /**
     * Изменение статуса заявок на участие в событии пользователя
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @param request Запрос на изменение
     * @return Результат запроса
     */
    EventRequestStatusUpdateResult patchStatusRequestCurrentUser(long userId,
                                                                 long eventId,
                                                                 EventRequestStatusUpdateRequest request);

    /**
     * Поиск событий админом по критериям
     *
     * @param searchAdmin Критерии
     * @return События
     */
    List<EventFullDto> getEventsAdmin(SearchAdmin searchAdmin);

    /**
     * Изменение события админом
     *
     * @param eventId                 ID события
     * @param updateEventAdminRequest Данные на обновление
     * @return Обновленное событие
     */
    EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    /**
     * Поиск событий по критериям
     *
     * @param searchUser Критерии
     * @param request    HTTP-запрос
     * @return События
     */
    List<EventShortDto> getEventUser(SearchUser searchUser, HttpServletRequest request);

    /**
     * Вывод события по ID
     *
     * @param eventId ID события
     * @param request HTTP-запрос
     * @return Событие
     */
    EventFullDto getEventId(long eventId, HttpServletRequest request);
}
