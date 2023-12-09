package ru.practicum.ewm.event.exception;

public class ChangePublishedEventException extends RuntimeException {
    public ChangePublishedEventException() {
        super("Нельзя изменять опубликованное событие");
    }
}