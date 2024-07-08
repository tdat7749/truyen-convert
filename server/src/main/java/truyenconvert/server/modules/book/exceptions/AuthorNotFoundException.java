package truyenconvert.server.modules.book.exceptions;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(String message){
        super(message);
    }
}
