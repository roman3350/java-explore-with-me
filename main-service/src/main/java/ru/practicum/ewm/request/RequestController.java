package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {
    private final RequestService requestService;

    /**
     * Вывод запросов пользователя
     *
     * @param userId ID пользователя
     * @return Запросы
     */
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestCurrentUser(@PathVariable long userId) {
        log.info("Вывод запросов пользователя с ID={}", userId);
        return requestService.getRequestCurrentUser(userId);
    }

    /**
     * Создание запроса
     *
     * @param userId  ID пользователя
     * @param eventId ID события
     * @return Созданный запрос
     */
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable long userId,
                                               @RequestParam(name = "eventId") long eventId) {
        log.info("Создание запроса пользователем с ID={}, На участие в событии с ID={}", userId, eventId);
        return requestService.postRequest(userId, eventId);
    }

    /**
     * Отмена запроса
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Отмененный запрос
     */
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("Отмена запроса пользователя с ID={}, в событии с ID={}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
