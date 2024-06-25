package truyenconvert.server.modules.users.exceptions;

public class AccountHasNotBeenLockedException extends RuntimeException{
    public AccountHasNotBeenLockedException(String message){
        super(message);
    }
}
