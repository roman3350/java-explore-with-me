package ru.practicum.ewm.request.exception;

public class EventNotPublishedException extends RuntimeException {
    public EventNotPublishedException() {
        super("Событие не опубликовано");
    }
}
