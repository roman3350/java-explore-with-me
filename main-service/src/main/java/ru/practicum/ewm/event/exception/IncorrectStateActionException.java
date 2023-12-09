package ru.practicum.ewm.event.exception;

public class IncorrectStateActionException extends RuntimeException {
    public IncorrectStateActionException() {
        super("Некорректное статус");
    }
}
