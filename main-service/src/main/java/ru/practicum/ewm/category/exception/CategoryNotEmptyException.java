package ru.practicum.ewm.category.exception;

public class CategoryNotEmptyException extends RuntimeException {
    public CategoryNotEmptyException() {
        super("The category is not empty");
    }
}
