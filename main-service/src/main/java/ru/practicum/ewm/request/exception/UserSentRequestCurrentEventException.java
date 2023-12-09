package ru.practicum.ewm.request.exception;

public class UserSentRequestCurrentEventException extends RuntimeException {
    public UserSentRequestCurrentEventException() {
        super("Пользователь отправлял запрос на участие в событии");
    }
}
