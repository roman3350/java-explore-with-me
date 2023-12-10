package ru.practicum.ewm.request.exception;

public class UserInitiatorEventException extends RuntimeException {
    public UserInitiatorEventException() {
        super("Пользователь инициатор события");
    }
}
