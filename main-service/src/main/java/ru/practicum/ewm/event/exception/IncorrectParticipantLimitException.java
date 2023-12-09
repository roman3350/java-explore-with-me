package ru.practicum.ewm.event.exception;

public class IncorrectParticipantLimitException extends RuntimeException {
    public IncorrectParticipantLimitException() {
        super("Лимит участников не может быть меньше участников, записавшихся на событие");
    }
}
