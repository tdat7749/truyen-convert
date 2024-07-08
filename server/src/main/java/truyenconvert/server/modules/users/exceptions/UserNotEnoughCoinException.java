package truyenconvert.server.modules.users.exceptions;

public class UserNotEnoughCoinException extends RuntimeException{
    public UserNotEnoughCoinException(String message){
        super(message);
    }
}
