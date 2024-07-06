package truyenconvert.server.modules.book.service;

import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Chapter;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.modules.book.dtos.CreateChapterDTO;
import truyenconvert.server.modules.book.dtos.EditChapterDTO;
import truyenconvert.server.modules.book.dtos.SetCoinDTO;
import truyenconvert.server.modules.book.exceptions.*;
import truyenconvert.server.modules.book.repositories.ChapterRepository;
import truyenconvert.server.modules.book.vm.ChapterDetailVm;
import truyenconvert.server.modules.book.vm.ChapterVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.common.service.UtilitiesService;
import truyenconvert.server.modules.users.exceptions.UserHasUnlockedChapterException;
import truyenconvert.server.modules.users.exceptions.UserNotEnoughCoinException;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.service.UserService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final BookService bookService;
    private final MessageService messageService;
    private final MappingService mappingService;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChapterServiceImpl.class);

    public ChapterServiceImpl(
            ChapterRepository chapterRepository,
            BookService bookService,
            MessageService messageService,
            MappingService mappingService,
            UserService userService
    ){
        this.bookService = bookService;
        this.mappingService = mappingService;
        this.messageService = messageService;
        this.chapterRepository = chapterRepository;
        this.userService = userService;
    }


    @Override
    public ResponseSuccess<Boolean> createChapter(CreateChapterDTO dto, User user) {
        var bookFound = bookService.findById(dto.getBookId()).orElse(null);
        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(bookFound.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(!bookFound.getUser().equals(user)){
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        int newestChaperOfBook = chapterRepository.getNewestChaperOfBook(bookFound) + 1;

        Chapter chapter = Chapter.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .chapter(newestChaperOfBook)
                .unLockCoin(dto.getUnLockCoin())
                .build();

        if(dto.getTimeExpired() != null){
            chapter.setTimeExpired(dto.getTimeExpired());
        }

        var save = chapterRepository.save(chapter);
        LOGGER.info("{} tạo một chương truyện ID = {}",user.getEmail(),save.getId());
        return new ResponseSuccess<>(messageService.getMessage("chapter.create.success"), true);

    }

    @Override
    public ResponseSuccess<Boolean> editChapter(EditChapterDTO dto,int id, User user) {
        var chapterFound = this.findById(id).orElse(null);
        if(chapterFound == null){
            throw new ChapterNotFoundException(messageService.getMessage("chapter.not-found"));
        }

        Book bookOfChapter = chapterFound.getBook();
        if(bookOfChapter.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(!chapterFound.getBook().getUser().equals(user)){
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        chapterFound.setContent(dto.getContent());
        chapterFound.setTitle(dto.getTitle());
        chapterFound.setUpdatedAt(LocalDateTime.now());
        chapterFound.setUnLockCoin(dto.getUnLockCoin());

        if(dto.getTimeExpired() != null){
            chapterFound.setTimeExpired(dto.getTimeExpired());
        }

        chapterRepository.save(chapterFound);
        LOGGER.info("{} chỉnh sửa một chương truyện ID = {}",user.getEmail(),chapterFound.getId());
        return new ResponseSuccess<>(messageService.getMessage("chapter.edit.success"), true);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<ChapterVm>>> getAllChapter(int pageIndex, String bookSlug) {

        var bookFound = bookService.findBySlug(bookSlug).orElse(null);

        if(bookFound == null){
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        Pageable paging = PageRequest.of(pageIndex,50, Sort.by(Sort.Direction.ASC,"chapter"));

        Page<Chapter> pagingResult = chapterRepository.getAllChapterOfBook(bookFound,paging);

        List<ChapterVm> chapterVmList = pagingResult.stream().map(mappingService::getChapterVm).toList();

        ResponsePaging<List<ChapterVm>> result = ResponsePaging.<List<ChapterVm>>builder()
                .pageSize(50)
                .pageIndex(pageIndex)
                .totalRecord(pagingResult.getTotalElements())
                .totalPage(pagingResult.getTotalPages())
                .data(chapterVmList)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    public ResponseSuccess<ChapterDetailVm> getChapterContent(int id,String hashCode,User user) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        var chapterFound = this.findById(id).orElse(null);
        if(chapterFound == null){
            throw new ChapterNotFoundException(messageService.getMessage("chapter.not-found"));
        }

        Book bookOfChapter = chapterFound.getBook();
        if(bookOfChapter.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }
        boolean isUnlocked = false;

        if(user != null){
            isUnlocked = chapterRepository.existsByUserAndChapter(user.getId(),chapterFound.getId());
        }

        ChapterDetailVm chapterDetailVm = mappingService.getChapterDetailVm(chapterFound,hashCode,isUnlocked);

        return new ResponseSuccess<>("Thành công.",chapterDetailVm);
    }

    @Override
    @Transactional
    public ResponseSuccess<Boolean> unlockChapter(int chapterId, User user) {
        var chapterFound = this.findById(chapterId).orElse(null);
        if(chapterFound == null){
            throw new ChapterNotFoundException(messageService.getMessage("chapter.not-found"));
        }

        Book bookOfChapter = chapterFound.getBook();
        if(bookOfChapter.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(chapterFound.getUnLockCoin() == 0 || (chapterFound.getTimeExpired() != null && chapterFound.getTimeExpired().isBefore(LocalDateTime.now()))){
            throw new ChapterIsFreeException(messageService.getMessage("chapter.is-free"));
        }

        if(chapterFound.getUnLockCoin() > user.getCoin()){
            throw new UserNotEnoughCoinException(messageService.getMessage("user.not-enough-coin"));
        }

        boolean isUnlocked = chapterRepository.existsByUserAndChapter(user.getId(),chapterFound.getId());

        if(isUnlocked){
            throw new UserHasUnlockedChapterException(messageService.getMessage("user.has-unlocked-chapter"));
        }
        // lỗi lazy load nên phải find user
        var userFound = userService.findById(user.getId()).orElse(null);
        if(userFound == null){
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }

        // thêm vào List mỗi đối tượng
        chapterFound.getUserUnlock().add(userFound);
        userFound.getChapters().add(chapterFound);

        // trừ coin user
        userFound.setCoin(userFound.getCoin() - chapterFound.getUnLockCoin());

        // save
        chapterRepository.save(chapterFound);
        userService.save(userFound);
        LOGGER.info("{} mua một chương truyện ID = {} với {} coin.",user.getEmail(),chapterFound.getId(),chapterFound.getUnLockCoin());
        return new ResponseSuccess<>(messageService.getMessage("chapter.un-lock.success"), true);
    }

    @Override
    public ResponseSuccess<Boolean> deleteChapter(int chapterId, User user) {
        var chapterFound = this.findById(chapterId).orElse(null);
        if(chapterFound == null){
            throw new ChapterNotFoundException(messageService.getMessage("chapter.not-found"));
        }

        Book bookOfChapter = chapterFound.getBook();
        if(bookOfChapter.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(user.getRole() == Role.MODERATOR || user.getRole() == Role.ADMIN){
            chapterRepository.delete(chapterFound);
            return new ResponseSuccess<>(messageService.getMessage("chapter.delete.success"), true);
        }

        if(!bookOfChapter.getUser().equals(user)) {
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        chapterRepository.delete(chapterFound);
        LOGGER.info("{} {} xóa chương truyện ID = {}",user.getRole().name(),user.getEmail(),chapterFound.getId());
        return new ResponseSuccess<>(messageService.getMessage("chapter.delete.success"), true);
    }

    @Override
    public ResponseSuccess<ChapterVm> setCoinForChapter(int chapterId, SetCoinDTO dto, User user) {
        var chapterFound = this.findById(chapterId).orElse(null);
        if(chapterFound == null){
            throw new ChapterNotFoundException(messageService.getMessage("chapter.not-found"));
        }

        Book bookOfChapter = chapterFound.getBook();
        if(bookOfChapter.isDeleted()){
            throw new BookHadBeenDeletedException(messageService.getMessage("book.had-been-deleted"));
        }

        if(!bookOfChapter.getUser().equals(user)) {
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        if(!bookOfChapter.isVip()){
            throw new BookIsNotInVipException(messageService.getMessage("book.is-not-in-vip"));
        }

        chapterFound.setUnLockCoin(dto.getCoin());
        if(dto.getTimeExpired() != null){
            chapterFound.setTimeExpired(dto.getTimeExpired());
        }

        var save = chapterRepository.save(chapterFound);
        ChapterVm chapterVm = mappingService.getChapterVm(save);

        LOGGER.info("{} vừa set {} coin cho chương truyện ID = {}",user.getEmail(),dto.getCoin(),chapterFound.getId());

        return new ResponseSuccess<>(messageService.getMessage("chapter.set-coin"),chapterVm);
    }

    @Override
    public Optional<Chapter> findById(int id) {
        return chapterRepository.findById(id);
    }
}
