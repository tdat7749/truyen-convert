package truyenconvert.server.modules.book.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class BookExceptionHandler {
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError authorNotFoundExceptionHandler(AuthorNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError bookNotFoundExceptionHandler(BookNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(BookHadBeenDeletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError bookHadBeenDeletedExceptionHandler(BookHadBeenDeletedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(BookIsNotInVipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError bookIsNotInVipExceptionHandler(BookIsNotInVipException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(ChapterIsFreeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError chapterIsFreeExceptionHandler(ChapterIsFreeException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(NotCreaterOfBookException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError notCreaterOfBookExceptionHandler(NotCreaterOfBookException ex){
        return new ResponseError(HttpStatus.FORBIDDEN,403,ex.getMessage());
    }

    @ExceptionHandler(ChapterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError chapterNotFoundExceptionHandler(ChapterNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,400,ex.getMessage());
    }

    @ExceptionHandler(BookSlugUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError bookSlugUsedExceptionHandler(BookSlugUsedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(BookAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError bookAlreadyExistExceptionHandler(BookAlreadyExistException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
