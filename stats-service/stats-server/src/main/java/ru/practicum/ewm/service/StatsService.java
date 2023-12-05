package ru.practicum.ewm.service;

import ru.practicum.ewm.EndPointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.util.List;

public interface StatsService {

    /**
     * Запрос на создание EndPointa
     *
     * @param endPointHitDto EndPoint
     * @return Подтверждение создания EndPoint
     */
    String postEndPointHit(EndPointHitDto endPointHitDto);

    /**
     * Вывод статистики по EndPoint
     *
     * @param start  Начало временного промежутка
     * @param end    Конец временного промежутка
     * @param uris   URI
     * @param unique Уникальные посещения
     * @return Статистика посещения
     */
    List<ViewStatsDto> getViewStats(String start, String end, String[] uris, boolean unique);
}
