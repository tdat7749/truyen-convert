package truyenconvert.server.modules.book.exceptions;

public class ChapterNotFoundException extends RuntimeException{
    public ChapterNotFoundException(String message){
        super(message);
    }
}
