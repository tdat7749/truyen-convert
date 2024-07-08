package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Author;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.dtos.EditBookDTO;
import truyenconvert.server.modules.book.exceptions.BookAlreadyExistException;
import truyenconvert.server.modules.book.exceptions.BookHadBeenDeletedException;
import truyenconvert.server.modules.book.exceptions.BookNotFoundException;
import truyenconvert.server.modules.book.exceptions.NotCreaterOfBookException;
import truyenconvert.server.modules.book.repositories.BookRepository;
import truyenconvert.server.modules.book.vm.BookVm;
import truyenconvert.server.modules.classifies.exceptions.CategoryNotFoundException;
import truyenconvert.server.modules.classifies.exceptions.SectNotFoundException;
import truyenconvert.server.modules.classifies.exceptions.WorldContextNotFoundException;
import truyenconvert.server.modules.classifies.service.CategoryService;
import truyenconvert.server.modules.classifies.service.SectService;
import truyenconvert.server.modules.classifies.service.WorldContextService;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.storage.service.S3FileStorageService;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final S3FileStorageService s3FileStorageService;
    private final MessageService messageService;
    private final MappingService mappingService;
    private final AuthorService authorService;
    private final WorldContextService worldContextService;
    private final CategoryService categoryService;
    private final SectService sectService;
    private final UserService userService;

    public BookServiceImpl(
            BookRepository bookRepository,
            S3FileStorageService s3FileStorageService,
            MessageService messageService,
            MappingService mappingService,
            AuthorService authorService,
            WorldContextService worldContextService,
            CategoryService categoryService,
            SectService sectService,
            UserService userService

    ){
        this.bookRepository = bookRepository;
        this.s3FileStorageService = s3FileStorageService;
        this.messageService = messageService;
        this.mappingService = mappingService;
        this.authorService = authorService;
        this.worldContextService = worldContextService;
        this.sectService = sectService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Override
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findBySlug(String slug) {
        return bookRepository.findBySlug(slug);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public ResponseSuccess<BookVm> createBook(CreateBookDTO dto, User user) {
        var bookFound = bookRepository.findByOriginalNameOrOriginalLink(dto.getOriginalName(),dto.getOriginalLink()).orElse(null);
        if(bookFound != null){
            throw new BookAlreadyExistException(messageService.getMessage("book.exists"));
        }

        var bookFoundBySlug = this.findBySlug(dto.getSlug()).orElse(null);
        if(bookFoundBySlug != null){
            dto.setSlug(dto.getSlug() + "-" + bookFoundBySlug.getId());
        }

        var sectFound = sectService.findById(dto.getSectId()).orElse(null);
        if(sectFound == null){
            throw new SectNotFoundException(messageService.getMessage("sect.not-found"));
        }

        var worldContextFound = worldContextService.findById(dto.getWorldContextId()).orElse(null);
        if(worldContextFound == null){
            throw new WorldContextNotFoundException(messageService.getMessage("world-context.not-found"));
        }

        var categoryFound = categoryService.findById(dto.getCategoryId()).orElse(null);
        if(categoryFound == null){
            throw new CategoryNotFoundException(messageService.getMessage("category.not-found"));
        }

        Author author = authorService.createAuthor(dto.getAuthorName(),dto.getOriginalAuthorName());

        Book book = Book.builder()
                .title(dto.getTitle())
                .introduction(dto.getIntroduction())
                .slug(dto.getSlug())
                .user(user)
                .author(author)
                .originalLink(dto.getOriginalLink())
                .originalName(dto.getOriginalName())
                .thumbnail(dto.getThumbnail())
                .category(categoryFound)
                .worldContext(worldContextFound)
                .sect(sectFound)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .newChapAt(LocalDateTime.now())
                .build();

        BookVm bookVm = mappingService.getBookVm(book);

        return new ResponseSuccess<>(messageService.getMessage("book.create.success"),bookVm);
    }

    @Override
    public ResponseSuccess<Boolean> setVip(int bookId, User user) {
        var bookFound = this.findById(bookId).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(bookFound.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(!bookFound.getUser().equals(user) && user.getRole() == Role.USER){
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        bookFound.setVip(true);
        bookFound.setUpdatedAt(LocalDateTime.now());

        this.save(bookFound);

        return new ResponseSuccess<>(messageService.getMessage("book.set-vip"), true);

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
    public ResponseSuccess<BookVm> getBookBySlug(String slug) {
        var bookFound = this.findBySlug(slug).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(bookFound.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if((bookFound.getState() != BookState.Published)){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        BookVm bookVm = mappingService.getBookVm(bookFound);
        long countChapter = this.getTotalChapterOfBook(bookFound);
        bookVm.setCountChapter(countChapter);

        return new ResponseSuccess<>("Thành công.",bookVm);
    }

    @Override
    public ResponseSuccess<BookVm> getBookById(int id) {
        var bookFound = this.findById(id).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        BookVm bookVm = mappingService.getBookVm(bookFound);

        return new ResponseSuccess<>("Thành công.",bookVm);
    }

    @Override
    public ResponseSuccess<List<BookVm>> getAllPublicBook(int limits, int pageIndex, String sortBy, String keyword, int world, int sect, int cate, BookStatus status, int isVip) {
        return null;
    }

    @Override
    public ResponseSuccess<List<BookVm>> getAllBook(int limits, int pageIndex, String sortBy, String keyword, int world, int sect, int cate, BookStatus status, BookState state, int isVip) {
        return null;
    }


    @Override
    public ResponseSuccess<Boolean> changeCreaterOfBook(int userId,int bookId, User user) {
        var userFound = userService.findById(userId).orElse(null);
        if(userFound == null){
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }

        var bookFound = this.findById(bookId).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(bookFound.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        bookFound.setUser(userFound);
        bookFound.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(bookFound);

        return new ResponseSuccess<>(messageService.getMessage("book.change-creater.success"), true);
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
        var bookFound = this.findById(bookId).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(bookFound.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(!bookFound.getUser().equals(user) && user.getRole() == Role.USER){
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        bookFound.setVip(false);
        bookFound.setUpdatedAt(LocalDateTime.now());

        this.save(bookFound);

        return new ResponseSuccess<>(messageService.getMessage("book.un-vip"), true);
    }

    private Long getTotalChapterOfBook(Book book) {
        Long total = bookRepository.getTotalChapterOfBook(book);
        if(total == null){
            return 0L;
        }
        return total;
    }
}
