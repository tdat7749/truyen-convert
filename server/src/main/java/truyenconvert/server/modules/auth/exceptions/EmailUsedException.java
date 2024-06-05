package truyenconvert.server.modules.auth.exceptions;

public class EmailUsedException extends RuntimeException{
    public EmailUsedException(String message){
        super(message);
    }
}
