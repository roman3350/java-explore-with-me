package ru.practicum.ewm.utilites;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.IncorrectDataException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({IncorrectDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
