package ru.practicum.ewm.user.exception;

public class DuplicateUserNameException extends RuntimeException {
    public DuplicateUserNameException() {
        super("Пользователь с таким именем существует");
    }
}
