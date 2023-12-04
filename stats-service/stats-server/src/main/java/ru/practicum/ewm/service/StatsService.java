package ru.practicum.ewm.service;

import ru.practicum.ewm.EndPointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.util.List;

public interface StatsService {

    String postEndPointHit(EndPointHitDto endPointHitDto);

    List<ViewStatsDto> getViewStats(String start, String end, String[] uris, boolean unique);
}
