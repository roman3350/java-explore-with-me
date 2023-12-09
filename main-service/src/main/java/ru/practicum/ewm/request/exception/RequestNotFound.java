package ru.practicum.ewm.request.exception;

public class RequestNotFound extends RuntimeException {
    public RequestNotFound(long requestId) {
        super("Request with id=" + requestId + " was not found");
    }
}
