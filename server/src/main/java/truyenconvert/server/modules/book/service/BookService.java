package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.dtos.EditBookDTO;
import truyenconvert.server.modules.book.vm.BookDetailVm;
import truyenconvert.server.modules.book.vm.BookVm;

import java.util.List;

@Service
public interface BookService {
    ResponseSuccess<Boolean> createBook(CreateBookDTO dto, User user);
    ResponseSuccess<Boolean> setVip(int bookId, User user); // để có thể set tiền chương truyện
    ResponseSuccess<Boolean> editBook(EditBookDTO dto, User user);
    ResponseSuccess<String> changeThumbnail(MultipartFile file, User user);
    ResponseSuccess<BookDetailVm> getBookBySlug(String slug);
    ResponseSuccess<BookDetailVm> getBookById(int id);
    ResponseSuccess<List<BookVm>> getAllPublicBook(int limits, int pageIndex, String sortBy, String keyword, int world, int sect, int cate, int status, int isVip);
    ResponseSuccess<List<BookVm>> getAllBook(int limits,int pageIndex, String sortBy,String keyword,int world,int sect, int cate,int status, int state,int isVip);
    ResponseSuccess<Boolean> changePosterOfBook(int userId, User user);
    ResponseSuccess<List<BookVm>> getAllBookOfUser(int limits,int pageIndex, String sortBy,String keyword,int status, int state,int isVip);
    ResponseSuccess<Boolean> deleteBook(int bookId,User user); // phải là admin hoặc mod mới được xóa, nếu user muốn xóa thì dùng report yêu cầu xóa.
    ResponseSuccess<Boolean> softDeleteBook(int bookId,User user);
    ResponseSuccess<Boolean> changeBookStatus(int bookId,User user);
    ResponseSuccess<Boolean> unVip(int bookId,User user); // khi unvip thì sẽ hủy mua toàn bộ các chương truyện -> thành free hết.

    // get book by creater
    // get book user marked
}
