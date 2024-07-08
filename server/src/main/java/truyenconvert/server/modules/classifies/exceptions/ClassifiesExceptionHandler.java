package truyenconvert.server.modules.classifies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class ClassifiesExceptionHandler{
    @ExceptionHandler(WorldContextNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError worldContextNotFoundExceptionHandler(WorldContextNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(SectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError sectNotFoundExceptionHandler(SectNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError categoryNotFoundExceptionHandler(CategoryNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }
}
