package truyenconvert.server.modules.read_histories.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.ReadHistory;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.exceptions.BookNotFoundException;
import truyenconvert.server.modules.book.service.BookService;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.read_histories.exceptions.NotReadHistoryOwnerException;
import truyenconvert.server.modules.read_histories.exceptions.ReadHistoryNotFoundException;
import truyenconvert.server.modules.read_histories.repositories.ReadHistoryRepository;
import truyenconvert.server.modules.read_histories.vm.ReadHistoryVm;
import truyenconvert.server.modules.read_histories.dtos.SaveHistoryDTO;
import truyenconvert.server.modules.redis.service.RedisService;

import java.time.LocalDateTime;
import java.util.List;

public class ReadHistoryServiceImpl implements ReadHistoryService{

    private final String CACHE_VALUE = "read_histories";
    private final int READ_HISTORY_PAGE_SIZE = 5;
    private final String READ_HISTORY_SORT_BY = "createdAt";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadHistoryServiceImpl.class);

    private final ReadHistoryRepository readHistoryRepository;
    private final MessageService messageService;
    private final MappingService mappingService;
    private final RedisService redisService;
    private final BookService bookService;


    public ReadHistoryServiceImpl(
            ReadHistoryRepository readHistoryRepository,
            MessageService messageService,
            MappingService mappingService,
            RedisService redisService,
            BookService bookService
    ){
        this.readHistoryRepository = readHistoryRepository;
        this.messageService = messageService;
        this.mappingService = mappingService;
        this.redisService = redisService;
        this.bookService = bookService;
    }

    @Override
    public ResponseSuccess<Boolean> saveHistory(SaveHistoryDTO dto, User user) {
        var bookFound = bookService.findById(dto.getBookId()).orElse(null);
        if(bookFound == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"),dto.getBookId());
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        var readHistoryFound = readHistoryRepository.findByBookAndUser(bookFound,user).orElse(null);
        if(readHistoryFound == null){
            ReadHistory readHistory = ReadHistory.builder()
                    .chapter(dto.getChapter())
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .book(bookFound)
                    .build();

            readHistoryFound = readHistoryRepository.save(readHistory);
        }else{
            readHistoryFound.setChapter(dto.getChapter());
            readHistoryFound.setUpdatedAt(LocalDateTime.now());

            readHistoryRepository.save(readHistoryFound);
        }
        LOGGER.info(messageService.getMessage("read-history.log.save.success"),user.getId(),readHistoryFound.getId());

        // sau khi thêm hoặc cập nhật thì xóa cache để có thể get dữ liệu mới nhất
        redisService.evictCachePrefixAndSuffixUserId(CACHE_VALUE, user.getId());

        return new ResponseSuccess<>(messageService.getMessage("read-history.save.success"),true);
    }

    @Override
    public ResponseSuccess<Boolean> deleteHistory(int id, User user) {
        var readHistoryFound = readHistoryRepository.findById(id).orElse(null);
        if(readHistoryFound == null){
            LOGGER.error(messageService.getMessage("read-history.log.not-found"),id);
            throw new ReadHistoryNotFoundException(messageService.getMessage("read-history.not-found"));
        }
        if(!readHistoryFound.getUser().equals(user)){
            LOGGER.error(messageService.getMessage("read-history.log.not-owner"),user.getId(),id);
            throw new NotReadHistoryOwnerException(messageService.getMessage("read-history.not-owner"));
        }

        readHistoryRepository.delete(readHistoryFound);

        LOGGER.info(messageService.getMessage("read-history.log.delete.success"),user.getId(),id);

        redisService.evictCachePrefixAndSuffixUserId(CACHE_VALUE, user.getId());
        return new ResponseSuccess<>(messageService.getMessage("read-history.delete.success"),true);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'pageIndex:' + '#pageIndex' + ',userId:' + #user.id")
    public ResponseSuccess<ResponsePaging<List<ReadHistoryVm>>> getAllReadHistory(int pageIndex, User user) {
        Pageable paging = PageRequest.of(pageIndex,READ_HISTORY_PAGE_SIZE, Sort.by(Sort.Direction.DESC,READ_HISTORY_SORT_BY));

        Page<ReadHistory> pagingResult = readHistoryRepository.getAllReadHistoryByUser(paging,user);

        List<ReadHistoryVm> data = pagingResult.getContent().stream().map(mappingService::getReadHistoryVm).toList();

        ResponsePaging<List<ReadHistoryVm>> responsePaging = ResponsePaging.<List<ReadHistoryVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(READ_HISTORY_PAGE_SIZE)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(data)
                .build();

        return new ResponseSuccess<>("Thành công.",responsePaging);
    }
}
