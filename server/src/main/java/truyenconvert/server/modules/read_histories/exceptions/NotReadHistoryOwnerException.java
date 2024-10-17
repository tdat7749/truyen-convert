package truyenconvert.server.modules.read_histories.exceptions;

public class NotReadHistoryOwnerException extends RuntimeException {
    public NotReadHistoryOwnerException(String message){
        super(message);
    }
}
