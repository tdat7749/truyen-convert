package truyenconvert.server.modules.comment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class CommentExceptionHandler {
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError commentNotFoundExceptionHandler(CommentNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(NotCreaterOfCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError notCreaterOfCommentExceptionHandler(NotCreaterOfCommentException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
