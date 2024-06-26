package truyenconvert.server.modules.users.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class UserExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError userNotFoundExceptionHandler(UserNotFoundException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.NOT_FOUND,404, ex.getMessage());
    }

    @ExceptionHandler(AccountHasNotBeenLockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError accountHasNotBeenLockedExceptionHandler(AccountHasNotBeenLockedException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(AccountHasBeenLockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError accountHasBeenLockedExceptionHandler(AccountHasBeenLockedException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(UserHasNotVerifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError userHasNotVerifiedExceptionHandler(UserHasNotVerifiedException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(CurrentPasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError currentPasswordNotMatchExceptionHandler(CurrentPasswordNotMatchException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(NewPasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError newPasswordNotMatchExceptionHandler(NewPasswordNotMatchException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }
}
