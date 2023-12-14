package ru.practicum.ewm.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(long commentId) {
        super("Комментария с ID=" + commentId + " не существует");
    }
}
