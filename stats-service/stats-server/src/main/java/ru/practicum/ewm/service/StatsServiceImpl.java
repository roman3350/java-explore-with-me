package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndPointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.model.EndPointHitMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository statsRepository;

    @Override
    public String postEndPointHit(EndPointHitDto endPointHitDto) {
        statsRepository.save(EndPointHitMapper.toEndpointHit(endPointHitDto));
        return "Информация сохранена";
    }

    @Override
    public List<ViewStatsDto> getViewStats(String start, String end, String[] uris, boolean unique) {
        LocalDateTime startDataTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDataTime = LocalDateTime.parse(end, FORMATTER);
        if (uris != null && unique) {
            return statsRepository.getStatsUriAndUnique(startDataTime, endDataTime, List.of(uris));
        }
        if (uris != null) {
            return statsRepository.getStatsUri(startDataTime, endDataTime, List.of(uris));
        }
        if (unique) {
            return statsRepository.getStatsUnique(startDataTime, endDataTime);
        }
        return statsRepository.getStats(startDataTime, endDataTime);
    }
}
