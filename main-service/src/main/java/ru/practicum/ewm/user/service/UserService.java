package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Вывод пользователей
     *
     * @param ids  ID пользователей
     * @param from с какого элемента выводить
     * @param size количество элементов на странице
     * @return Пользователи
     */
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    /**
     * Создание пользователя
     *
     * @param newUserRequest Данные пользователя
     * @return Созданный пользователь
     */
    UserDto postUser(NewUserRequest newUserRequest);

    /**
     * Удаление пользователя
     *
     * @param userId ID пользователя
     */
    void deleteUser(long userId);
}
