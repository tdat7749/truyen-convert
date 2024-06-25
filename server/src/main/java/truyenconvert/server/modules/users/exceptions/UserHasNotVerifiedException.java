package truyenconvert.server.modules.users.exceptions;

public class UserHasNotVerifiedException extends RuntimeException{
    public UserHasNotVerifiedException(String message){
        super(message);
    }
}
