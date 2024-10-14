package truyenconvert.server.modules.comment.exceptions;

public class UserHasNotLikedCommentException extends  RuntimeException{
    public UserHasNotLikedCommentException(String message){
        super(message);
    }
}
