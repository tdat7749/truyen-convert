package truyenconvert.server.modules.users.exceptions;

public class CurrentPasswordNotMatchException extends RuntimeException{
    public CurrentPasswordNotMatchException(String message){
        super(message);
    }
}
