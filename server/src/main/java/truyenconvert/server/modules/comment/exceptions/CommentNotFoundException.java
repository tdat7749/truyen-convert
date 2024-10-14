package truyenconvert.server.modules.comment.exceptions;


public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message){
        super(message);
    }
}
