package truyenconvert.server.modules.users.exceptions;


public class AccountHasBeenLockedException extends RuntimeException{
    public AccountHasBeenLockedException(String message){
        super(message);
    }
}
