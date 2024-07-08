package truyenconvert.server.modules.book.exceptions;

public class BookSlugUsedException extends RuntimeException{
    public BookSlugUsedException(String message){
        super(message);
    }
}
