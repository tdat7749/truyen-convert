package truyenconvert.server.modules.book.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.*;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.dtos.EditBookDTO;
import truyenconvert.server.modules.book.exceptions.*;
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
import truyenconvert.server.modules.report.service.ReportServiceImpl;
import truyenconvert.server.modules.storage.service.S3FileStorageService;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final String S3_BUCKET = "truyencv";
    private final String CACHE_VALUE = "books";
    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

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

    ) {
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
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<BookVm> createBook(CreateBookDTO dto, User user) {
        try{
            var bookFound = bookRepository.findByOriginalNameOrOriginalLink(dto.getOriginalName(), dto.getOriginalLink()).orElse(null);
            if (bookFound != null) {
                LOGGER.error(messageService.getMessage("book.log.exists-name-link"),dto.getOriginalName(),dto.getOriginalLink());
                throw new BookAlreadyExistException(messageService.getMessage("book.exists"));
            }

            var bookFoundBySlug = this.findBySlug(dto.getSlug()).orElse(null);
            if (bookFoundBySlug != null) {
                dto.setSlug(dto.getSlug() + "-" + bookFoundBySlug.getId());
            }

            var sectFound = sectService.findById(dto.getSectId()).orElse(null);
            if (sectFound == null) {
                LOGGER.error(messageService.getMessage("sect.log.not-found"),dto.getSectId());
                throw new SectNotFoundException(messageService.getMessage("sect.not-found"));
            }

            var worldContextFound = worldContextService.findById(dto.getWorldContextId()).orElse(null);
            if (worldContextFound == null) {
                LOGGER.error(messageService.getMessage("world-context.log.not-found"),dto.getWorldContextId());
                throw new WorldContextNotFoundException(messageService.getMessage("world-context.not-found"));
            }

            var categoryFound = categoryService.findById(dto.getCategoryId()).orElse(null);
            if (categoryFound == null) {
                LOGGER.error(messageService.getMessage("category.log.not-found"),dto.getCategoryId());
                throw new CategoryNotFoundException(messageService.getMessage("category.not-found"));
            }

            Author author = authorService.createAuthor(dto.getAuthorName(), dto.getOriginalAuthorName());

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

            LOGGER.info(messageService.getMessage("book.log.create.success"),user.getId(),book.getId());

            return new ResponseSuccess<>(messageService.getMessage("book.create.success"),bookVm);
        }catch (Exception e){

            s3FileStorageService.deleteFile(dto.getThumbnail(),S3_BUCKET);

            return new ResponseSuccess<>(messageService.getMessage("book.create.failed"), null);
        }
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> setVip(int bookId, User user) {
        var bookFound = this.findById(bookId).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"),bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if (bookFound.isDeleted()) {
            LOGGER.error(messageService.getMessage("book.log.had-been-deleted"),bookId);
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if (!bookFound.getUser().equals(user) && user.getRole() == Role.USER) {
            LOGGER.error(messageService.getMessage("book.log.not-the-creater"),user.getId(),bookId);
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        bookFound.setVip(true);
        bookFound.setUpdatedAt(LocalDateTime.now());

        this.save(bookFound);
        LOGGER.info(messageService.getMessage("book.log.set-vip"),user.getId(),bookId);
        return new ResponseSuccess<>(messageService.getMessage("book.set-vip"), true);

    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<BookVm> editBook(EditBookDTO dto, int id, User user) {
        var bookFoundById = bookRepository.findById(id).orElse(null);
        if(bookFoundById == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"), id);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }
        if(!dto.getOriginalName().equals(bookFoundById.getOriginalName()) && !dto.getOriginalLink().equals(bookFoundById.getOriginalLink())) {
            var bookFound = bookRepository.findByOriginalNameOrOriginalLink(dto.getOriginalName(), dto.getOriginalLink()).orElse(null);
            if (bookFound != null) {
                LOGGER.error(messageService.getMessage("book.log.exists-name-link"));
                throw new BookAlreadyExistException(messageService.getMessage("book.exists"));
            }
        }
        if(!bookFoundById.getSlug().equals(dto.getSlug())){
            var bookFoundBySlug = this.findBySlug(dto.getSlug()).orElse(null);
            if (bookFoundBySlug != null) {
                LOGGER.error(messageService.getMessage("book.log.slug-used"),dto.getSlug(),bookFoundBySlug.getId());
                throw new BookSlugUsedException(messageService.getMessage("book.slug-used"));
            }
        }

        var sectFound = sectService.findById(dto.getSectId()).orElse(null);
        if (sectFound == null) {
            LOGGER.error(messageService.getMessage("sect.log.not-found"),dto.getSectId());
            throw new SectNotFoundException(messageService.getMessage("sect.not-found"));
        }

        var worldContextFound = worldContextService.findById(dto.getWorldContextId()).orElse(null);
        if (worldContextFound == null) {
            LOGGER.error(messageService.getMessage("world-context.log.not-found"), dto.getWorldContextId());
            throw new WorldContextNotFoundException(messageService.getMessage("world-context.not-found"));
        }

        var categoryFound = categoryService.findById(dto.getCategoryId()).orElse(null);
        if (categoryFound == null) {
            LOGGER.error(messageService.getMessage("category.log.not-found"), dto.getCategoryId());
            throw new CategoryNotFoundException(messageService.getMessage("category.not-found"));
        }

        Author author = authorService.createAuthor(dto.getAuthorName(), dto.getOriginalAuthorName());

        bookFoundById.setTitle(dto.getTitle());
        bookFoundById.setSlug(dto.getSlug());
        bookFoundById.setOriginalLink(dto.getOriginalLink());
        bookFoundById.setOriginalName(dto.getOriginalName());
        bookFoundById.setAuthor(author);
        bookFoundById.setWorldContext(worldContextFound);
        bookFoundById.setSect(sectFound);
        bookFoundById.setCategory(categoryFound);
        bookFoundById.setUpdatedAt(LocalDateTime.now());

        var newSaveBook = bookRepository.save(bookFoundById);

        BookVm bookVm = mappingService.getBookVm(newSaveBook);

        LOGGER.info(messageService.getMessage("book.log.changed.success"),user.getId(),bookFoundById.getId());
        return new ResponseSuccess<>(messageService.getMessage("book.changed.success"),bookVm);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<String> changeThumbnail(MultipartFile file, User user) {
        return null;
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "#slug")
    public ResponseSuccess<BookVm> getBookBySlug(String slug) {
        var bookFound = this.findBySlug(slug).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found-slug"), slug);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if (bookFound.isDeleted()) {
            LOGGER.error(messageService.getMessage("book.log.had-been-deleted-slug"));
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if ((bookFound.getState() != BookState.Published)) {
            LOGGER.error(messageService.getMessage("book.log.unpublished"), bookFound.getId());
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        BookVm bookVm = mappingService.getBookVm(bookFound);
        long countChapter = this.getTotalChapterOfBook(bookFound);
        bookVm.setCountChapter(countChapter);

        return new ResponseSuccess<>("Thành công.", bookVm);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "#id")
    public ResponseSuccess<BookVm> getBookById(int id) {
        var bookFound = this.findById(id).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"), id);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        BookVm bookVm = mappingService.getBookVm(bookFound);

        return new ResponseSuccess<>("Thành công.", bookVm);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'limits:' + #limits + ',pageIndex:' + #pageIndex + ',sort:' + #sort + ',keyword:' + #keyword + ',world:' + #world + ',sect:' + #sect + ',cate:' + #cate + ',isVip:' + #isVip")
    public ResponseSuccess<ResponsePaging<List<BookVm>>> getAllPublicBook(int limits, int pageIndex, String sort, String keyword, Integer world, Integer sect, Integer cate, Integer isVip) {
        WorldContext worldContext = null;
        Sect sectt = null;
        Category category = null;
        var sortBy = Sort.by(Sort.Direction.DESC,"newChapAt");
        if(sort.equals("newest")){
            sortBy = Sort.by(Sort.Direction.DESC,"createdAt");
        }else if (sort.equals("oldest")){
            sortBy = Sort.by(Sort.Direction.ASC,"createdAt");
        }

        if(world != null){
            worldContext = this.worldContextService.findById(world).orElse(null);
        }
        if(sect != null){
            sectt = this.sectService.findById(sect).orElse(null);
        }
        if(cate != null){
            category = this.categoryService.findById(cate).orElse(null);
        }

        Pageable paging = PageRequest.of(pageIndex, limits, sortBy);

        Page<Book> pagingResult = bookRepository.getAllPublicBook(paging,keyword,worldContext,sectt,category,BookState.Published,isVip);

        List<BookVm> listBookVm = pagingResult.stream().map(mappingService::getBookVm).toList();

        ResponsePaging<List<BookVm>> responsePaging = ResponsePaging.<List<BookVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(limits)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(listBookVm)
                .build();

        return new ResponseSuccess<>("Thành công.",responsePaging);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'limits:' + #limits + ',pageIndex:' + #pageIndex + ',sort:' + #sort + ',keyword:' + #keyword + ',world:' + #world + ',sect:' + #sect + ',cate:' + #cate + ',isVip:' + #isVip + ',bookStatus:' + #status + ',bookState:' + #state")
    public ResponseSuccess<ResponsePaging<List<BookVm>>> getAllBook(int limits, int pageIndex, String sort, String keyword, Integer world, Integer sect, Integer cate, BookStatus status, BookState state, Integer isVip) {
        WorldContext worldContext = null;
        Sect sectt = null;
        Category category = null;
        var sortBy = Sort.by(Sort.Direction.DESC,"newChapAt");
        if(sort.equals("newest")){
            sortBy = Sort.by(Sort.Direction.DESC,"createdAt");
        }else if (sort.equals("oldest")){
            sortBy = Sort.by(Sort.Direction.ASC,"createdAt");
        }

        if(world != null){
            worldContext = this.worldContextService.findById(world).orElse(null);
        }
        if(sect != null){
            sectt = this.sectService.findById(sect).orElse(null);
        }
        if(cate != null){
            category = this.categoryService.findById(cate).orElse(null);
        }

        Pageable paging = PageRequest.of(pageIndex, limits, sortBy);

        Page<Book> pagingResult = bookRepository.getAllBook(paging,keyword,worldContext,sectt,category,status,state,isVip);

        List<BookVm> listBookVm = pagingResult.stream().map(mappingService::getBookVm).toList();

        ResponsePaging<List<BookVm>> responsePaging = ResponsePaging.<List<BookVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(limits)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(listBookVm)
                .build();

        return new ResponseSuccess<>("Thành công.",responsePaging);
    }


    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> changeCreaterOfBook(int userId, int bookId, User user) {
        var userFound = userService.findById(userId).orElse(null);
        if (userFound == null) {
            LOGGER.error(messageService.getMessage("user.log.not-found"), user.getId());
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }

        var bookFound = this.findById(bookId).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if (bookFound.isDeleted()) {
            LOGGER.error(messageService.getMessage("book.log.had-been-deleted"), bookId);
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        bookFound.setUser(userFound);
        bookFound.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(bookFound);
        LOGGER.info(messageService.getMessage("book.log.change-creater.success"),user.getId(),userId);
        return new ResponseSuccess<>(messageService.getMessage("book.change-creater.success"), true);
    }

    @Override
    // them cacheable
    public ResponseSuccess<List<BookVm>> getAllBookOfUser(int limits, int pageIndex, String sortBy, String keyword, int status, int state, int isVip) {
        return null;
    }


    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteBook(int bookId, User user) {
        // chỉ admin mới được quyền delete
        var foundBook = bookRepository.findById(bookId).orElse(null);
        if(foundBook == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        bookRepository.delete(foundBook);
        // thêm hàm delele hình ảnh
        LOGGER.info(messageService.getMessage("book.log.deleted"),user.getId(),foundBook.getId());
        return new ResponseSuccess<>(messageService.getMessage("book.deleted"),true);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> softDeleteBook(int bookId, User user) {
        // chỉ admin với mod được soft delete
        var foundBook = bookRepository.findById(bookId).orElse(null);
        if(foundBook == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }
        foundBook.setDeleted(true);

        bookRepository.save(foundBook);
        LOGGER.info(messageService.getMessage("book.log.deleted"),user.getId(),foundBook.getId());
        return new ResponseSuccess<>(messageService.getMessage("book.deleted"),true);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> changeBookStatus(int bookId,BookStatus status, User user) {
        // chỉ có người sở hữu truyện mới được change status
        var foundBook = bookRepository.findById(bookId).orElse(null);
        if(foundBook == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(!foundBook.getUser().equals(user)){
            LOGGER.error(messageService.getMessage("book.log.not-the-creater"), user.getId(),bookId);
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        foundBook.setStatus(status);
        bookRepository.save(foundBook);
        LOGGER.info(messageService.getMessage("book.log.status-changed"),user.getId(),bookId);
        return new ResponseSuccess<>(messageService.getMessage("book.status-changed"),true);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> unVip(int bookId, User user) {
        var bookFound = this.findById(bookId).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if (bookFound.isDeleted()) {
            LOGGER.error(messageService.getMessage("book.log.had-been-deleted"), bookId);
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if (!bookFound.getUser().equals(user) && user.getRole() == Role.USER) {
            LOGGER.error(messageService.getMessage("book.log.not-the-creater"), user.getId(),bookId);
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        bookFound.setVip(false);
        bookFound.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(bookFound);
        LOGGER.info(messageService.getMessage("book.log.un-vip"),user.getId(),bookId);
        return new ResponseSuccess<>(messageService.getMessage("book.un-vip"), true);
    }

    @Override
    public Long getTotalChapterOfBook(Book book) {
        Long total = bookRepository.getTotalChapterOfBook(book);
        if (total == null) {
            return 0L;
        }
        return total;
    }
}
