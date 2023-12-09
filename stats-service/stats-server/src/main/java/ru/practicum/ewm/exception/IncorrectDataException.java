package ru.practicum.ewm.exception;

public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException() {
        super("Некорректные даты");
    }
}
