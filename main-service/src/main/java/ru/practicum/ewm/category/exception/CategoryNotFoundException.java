package ru.practicum.ewm.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(long catId) {
        super("Category with id=" + catId + " was not found");
    }
}
