package truyenconvert.server.modules.read_histories.exceptions;

public class ReadHistoryNotFoundException extends RuntimeException{
    public ReadHistoryNotFoundException(String message){
        super(message);
    }
}
