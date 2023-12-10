package ru.practicum.ewm.event.exception;

public class StatusNotConfirmedOrRejectedException extends RuntimeException {
    public StatusNotConfirmedOrRejectedException() {
        super("Статус может быть CONFIRM и PENDING");
    }
}
