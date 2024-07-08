package truyenconvert.server.modules.book.exceptions;

public class BookAlreadyExistException extends RuntimeException{
    public BookAlreadyExistException(String message){
        super(message);
    }
}
