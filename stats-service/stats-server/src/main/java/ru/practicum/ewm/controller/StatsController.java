package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.EndPointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.model.EndPointHit;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    /**
     * Запрос на создание EndPointa
     *
     * @param endPointHitDto EndPoint
     * @return Подтверждение создания EndPoint
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndPointHit postEndPointHit(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.info("Создание EndPointHit {}", endPointHitDto);
        return statsService.postEndPointHit(endPointHitDto);
    }

    /**
     * Вывод статистики по EndPoint
     *
     * @param start  Начало временного промежутка
     * @param end    Конец временного промежутка
     * @param uris   URI
     * @param unique Уникальные посещения
     * @return Статистика посещения
     */
    @GetMapping("/stats")
    public List<ViewStatsDto> getViewStats(@RequestParam(name = "start") String start,
                                           @RequestParam(name = "end") String end,
                                           @RequestParam(name = "uris", required = false) String[] uris,
                                           @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Вывод ViewStats start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statsService.getViewStats(start, end, uris, unique);
    }
}
