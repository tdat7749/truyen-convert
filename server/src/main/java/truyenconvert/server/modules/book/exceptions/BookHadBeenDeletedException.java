package truyenconvert.server.modules.book.exceptions;

public class BookHadBeenDeletedException extends RuntimeException{
    public BookHadBeenDeletedException(String message){
        super(message);
    }
}
