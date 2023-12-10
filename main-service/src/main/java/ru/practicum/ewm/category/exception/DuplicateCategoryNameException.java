package ru.practicum.ewm.category.exception;

public class DuplicateCategoryNameException extends RuntimeException {
    public DuplicateCategoryNameException() {
        super("Есть категория с таким названием");
    }
}
