package ru.practicum.ewm.model;

import ru.practicum.ewm.EndPointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndPointHitMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndPointHit toEndpointHit(EndPointHitDto endPointHitDto) {
        String s = endPointHitDto.getTimestamp().replace('T',' ').substring(0, 19);
        return EndPointHit.builder()
                .app(endPointHitDto.getApp())
                .uri(endPointHitDto.getUri())
                .ip(endPointHitDto.getIp())
                .timestamp(LocalDateTime.parse(s, FORMATTER))
                .build();
    }
}
