package ru.practicum.ewm.event.exception;

public class ParticipantLimitReachedException extends RuntimeException {
    public ParticipantLimitReachedException() {
        super("Достигнут лимит участников события");
    }
}
