package ru.practicum.ewm.event.exception;

public class IncorrectEventDateException extends RuntimeException {
    public IncorrectEventDateException() {
        super("Некорректная дата");
    }
}
