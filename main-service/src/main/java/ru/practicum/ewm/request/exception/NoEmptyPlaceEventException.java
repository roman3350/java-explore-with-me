package ru.practicum.ewm.request.exception;

public class NoEmptyPlaceEventException extends RuntimeException {
    public NoEmptyPlaceEventException() {
        super("В событии нет свободных мест");
    }
}
