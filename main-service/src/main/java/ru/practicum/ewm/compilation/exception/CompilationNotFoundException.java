package ru.practicum.ewm.compilation.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(long compId) {
        super("Compilation with id=" + compId + " was not found");
    }
}
