package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.dtos.EditBookDTO;
import truyenconvert.server.modules.book.vm.BookVm;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    Optional<Book> findById(int id);
    Optional<Book> findBySlug(String slug);
    Book save(Book book);
    ResponseSuccess<BookVm> createBook(CreateBookDTO dto, User user);
    ResponseSuccess<Boolean> setVip(int bookId, User user); // để có thể set tiền chương truyện
    ResponseSuccess<BookVm> editBook(EditBookDTO dto,int id, User user);
    ResponseSuccess<String> changeThumbnail(MultipartFile file, User user);
    ResponseSuccess<BookVm> getBookBySlug(String slug);
    ResponseSuccess<BookVm> getBookById(int id);
    ResponseSuccess<ResponsePaging<List<BookVm>>> getAllPublicBook(int limits, int pageIndex, String sortBy, String keyword, Integer world, Integer sect, Integer cate, Integer isVip);
    ResponseSuccess<ResponsePaging<List<BookVm>>> getAllBook(int limits, int pageIndex, String sortBy, String keyword, Integer world, Integer sect, Integer cate, BookStatus status, BookState state, Integer isVip);
    ResponseSuccess<Boolean> changeCreaterOfBook(int userId,int bookId, User user);
    ResponseSuccess<List<BookVm>> getAllBookOfUser(int limits,int pageIndex, String sortBy,String keyword,int status, int state,int isVip);
    ResponseSuccess<Boolean> deleteBook(int bookId,User user); // phải là admin hoặc mod mới được xóa, nếu user muốn xóa thì dùng report yêu cầu xóa.
    ResponseSuccess<Boolean> softDeleteBook(int bookId,User user);
    ResponseSuccess<Boolean> changeBookStatus(int bookId,BookStatus status,User user);
    ResponseSuccess<Boolean> unVip(int bookId,User user); // khi unvip thì sẽ hủy mua toàn bộ các chương truyện -> thành free hết.

    // get book by creater
    // get book user marked
}
