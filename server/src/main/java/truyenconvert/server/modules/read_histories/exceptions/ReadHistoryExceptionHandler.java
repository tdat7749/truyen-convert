package truyenconvert.server.modules.read_histories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class ReadHistoryExceptionHandler {
    @ExceptionHandler(ReadHistoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError readHistoryNotFoundExceptionHandler(ReadHistoryNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(NotReadHistoryOwnerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError notReadHistoryOwnerExceptionHandler(NotReadHistoryOwnerException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
