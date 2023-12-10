package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.exception.DuplicateUserNameException;
import ru.practicum.ewm.user.exception.UserNotFoundException;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Вывод пользователей
     *
     * @param ids  ID пользователей
     * @param from с какого элемента выводить
     * @param size количество элементов на странице
     * @return Пользователи
     */
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (ids == null || ids.isEmpty()) {
            return UserMapper.toUserDto(userRepository.findAll(page).getContent());
        } else {
            return UserMapper.toUserDto(userRepository.findAllByIdIn(ids, page));
        }
    }

    /**
     * Создание пользователя
     *
     * @param newUserRequest Данные пользователя
     * @return Созданный пользователь
     */
    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        if (userRepository.findFirstByName(newUserRequest.getName()) != null) {
            throw new DuplicateUserNameException();
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.newUserRequestToUser(newUserRequest)));
    }

    /**
     * Удаление пользователя
     *
     * @param userId ID пользователя
     */
    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }
}
