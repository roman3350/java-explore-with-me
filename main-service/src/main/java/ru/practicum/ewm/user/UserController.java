package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    /**
     * Вывод пользователей
     *
     * @param ids  ID пользователей
     * @param from с какого элемента выводить
     * @param size количество элементов на странице
     * @return Пользователи
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Вывод пользователей с IDs={}, from={}, size={}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    /**
     * Создание пользователя
     *
     * @param newUserRequest Данные пользователя
     * @return Созданный пользователь
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Создание пользователя {}", newUserRequest);
        return userService.postUser(newUserRequest);
    }

    /**
     * Удаление пользователя
     *
     * @param userId ID пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с ID={}", userId);
        userService.deleteUser(userId);
    }
}
