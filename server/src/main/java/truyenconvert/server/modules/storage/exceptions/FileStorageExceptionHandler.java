package truyenconvert.server.modules.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class FileStorageExceptionHandler {
    @ExceptionHandler(BucketNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError bucketNullExceptionHandler(BucketNullException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
