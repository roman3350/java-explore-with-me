package ru.practicum.ewm.comment.exception;

public class UserNotCreatorCommentException extends RuntimeException {
    public UserNotCreatorCommentException() {
        super("Пользователь не является создателем комментария");
    }
}
