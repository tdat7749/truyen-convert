package truyenconvert.server.modules.review.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class ReviewExceptionHandler {
    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError reviewNotFoundExceptionHandler(ReviewNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(WasReviewedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError wasReviewedExceptionHandler(WasReviewedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(InvalidReviewScoreException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError invalidReviewScoreExceptionHandler(InvalidReviewScoreException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
