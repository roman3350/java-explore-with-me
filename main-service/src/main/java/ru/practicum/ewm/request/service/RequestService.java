package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    /**
     * Вывод запросов пользователя
     *
     * @param userId ID пользователя
     * @return Запросы
     */
    List<ParticipationRequestDto> getRequestCurrentUser(long userId);

    /**
     * Создание запроса
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Созданный запрос
     */
    ParticipationRequestDto postRequest(long userId, long eventId);

    /**
     * Отмена запроса
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Отмененный запрос
     */
    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
