package truyenconvert.server.modules.users.exceptions;

public class NewPasswordNotMatchException extends RuntimeException{
    public NewPasswordNotMatchException(String message){
        super(message);
    }
}
