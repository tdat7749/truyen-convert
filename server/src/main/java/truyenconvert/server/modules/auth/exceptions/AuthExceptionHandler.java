package truyenconvert.server.modules.auth.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class AuthExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(EmailUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError emailUsedExceptionHandler(EmailUsedException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
