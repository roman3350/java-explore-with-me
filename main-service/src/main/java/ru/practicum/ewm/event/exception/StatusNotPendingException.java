package ru.practicum.ewm.event.exception;

public class StatusNotPendingException extends RuntimeException {
    public StatusNotPendingException() {
        super("Статус должен быть PENDING");
    }
}
