package ru.practicum.ewm.event.exception;

public class UserNotInitiatorException extends RuntimeException {
    public UserNotInitiatorException() {
        super("Пользователь не является инициатором события");
    }
}