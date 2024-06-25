package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.dtos.EditBookDTO;
import truyenconvert.server.modules.book.repositories.BookRepository;
import truyenconvert.server.modules.book.vm.BookDetailVm;
import truyenconvert.server.modules.book.vm.BookVm;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseSuccess<Boolean> createBook(CreateBookDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> setVip(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> editBook(EditBookDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<String> changeThumbnail(MultipartFile file, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<BookDetailVm> getBookBySlug(String slug) {
        return null;
    }

    @Override
    public ResponseSuccess<BookDetailVm> getBookById(int id) {
        return null;
    }

    @Override
    public ResponseSuccess<List<BookVm>> getAllPublicBook(int limits, int pageIndex, String sortBy, String keyword, int world, int sect, int cate, int status, int isVip) {
        return null;
    }

    @Override
    public ResponseSuccess<List<BookVm>> getAllBook(int limits, int pageIndex, String sortBy, String keyword, int world, int sect, int cate, int status, int state, int isVip) {
        return null;
    }


    @Override
    public ResponseSuccess<Boolean> changePosterOfBook(int userId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<BookVm>> getAllBookOfUser(int limits, int pageIndex, String sortBy, String keyword, int status, int state, int isVip) {
        return null;
    }


    @Override
    public ResponseSuccess<Boolean> deleteBook(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> softDeleteBook(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> changeBookStatus(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> unVip(int bookId, User user) {
        return null;
    }


}
