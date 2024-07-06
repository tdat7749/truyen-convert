package truyenconvert.server.modules.book.exceptions;

public class BookIsNotInVipException extends RuntimeException{
    public BookIsNotInVipException(String message){
        super(message);
    }
}
