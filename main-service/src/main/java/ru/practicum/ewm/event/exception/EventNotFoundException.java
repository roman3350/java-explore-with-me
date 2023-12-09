package ru.practicum.ewm.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(long eventId) {
        super("Event with id=" + eventId + " was not found");
    }
}
