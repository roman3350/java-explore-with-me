package ru.practicum.ewm.utilites;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.category.exception.CategoryNotEmptyException;
import ru.practicum.ewm.category.exception.CategoryNotFoundException;
import ru.practicum.ewm.category.exception.DuplicateCategoryNameException;
import ru.practicum.ewm.compilation.exception.CompilationNotFoundException;
import ru.practicum.ewm.event.exception.*;
import ru.practicum.ewm.request.exception.*;
import ru.practicum.ewm.user.exception.DuplicateUserNameException;
import ru.practicum.ewm.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({CategoryNotFoundException.class, CompilationNotFoundException.class, EventNotFoundException.class, RequestNotFound.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Throwable e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler({UserNotInitiatorException.class, IncorrectParticipantLimitException.class, IncorrectStateActionException.class,
    StatusNotConfirmedOrRejectedException.class, ChangePublishedEventException.class, ParticipantLimitReachedException.class,
            StatusNotPendingException.class, DuplicateCategoryNameException.class, DuplicateUserNameException.class,
            CategoryNotEmptyException.class, UserSentRequestCurrentEventException.class, UserInitiatorEventException.class,
            EventNotPublishedException.class, NoEmptyPlaceEventException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(Throwable e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({IncorrectEventDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Throwable e){
        return new ErrorResponse(e.getMessage());
    }
}
