package truyenconvert.server.modules.donation.exception;

public class NotEnoughCoinException extends RuntimeException{
    public NotEnoughCoinException(String message){
        super(message);
    }
}
