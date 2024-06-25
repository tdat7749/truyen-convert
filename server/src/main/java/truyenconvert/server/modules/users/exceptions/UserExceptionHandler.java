package truyenconvert.server.modules.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError userNotFoundExceptionHandler(UserNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404, ex.getMessage());
    }

    @ExceptionHandler(AccountHasNotBeenLockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError accountHasNotBeenLockedExceptionHandler(AccountHasNotBeenLockedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(AccountHasBeenLockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError accountHasBeenLockedExceptionHandler(AccountHasBeenLockedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(UserHasNotVerifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError userHasNotVerifiedExceptionHandler(UserHasNotVerifiedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }
}
