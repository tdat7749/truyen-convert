package truyenconvert.server.modules.review.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message){
        super(message);
    }
}
