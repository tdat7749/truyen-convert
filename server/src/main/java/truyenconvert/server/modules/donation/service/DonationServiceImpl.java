package truyenconvert.server.modules.donation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Donation;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.exceptions.BookNotFoundException;
import truyenconvert.server.modules.book.exceptions.NotCreaterOfBookException;
import truyenconvert.server.modules.book.service.BookService;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.donation.dtos.CreateDonationDTO;
import truyenconvert.server.modules.donation.exception.InvalidDonateCoinException;
import truyenconvert.server.modules.donation.exception.NotEnoughCoinException;
import truyenconvert.server.modules.donation.repository.DonationRepository;
import truyenconvert.server.modules.donation.vm.DonationVm;
import truyenconvert.server.modules.redis.service.RedisService;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    private final String CACHE_VALUE = "donations";
    private final int DONATION_PAGE_SIZE = 5;
    private final String DONATION_SORT_BY = "createdAt";
    private static final Logger LOGGER = LoggerFactory.getLogger(DonationServiceImpl.class);

    private final DonationRepository donationRepository;
    private final MappingService mappingService;
    private final MessageService messageService;
    private final UserService userService;
    private final BookService bookService;
    private final RedisService redisService;

    public DonationServiceImpl(
            DonationRepository donationRepository,
            MappingService mappingService,
            MessageService messageService,
            UserService userService,
            BookService bookService,
            RedisService redisService
    ) {
        this.donationRepository = donationRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
        this.userService = userService;
        this.bookService = bookService;
        this.redisService = redisService;
    }

    @Override
    public ResponseSuccess<Boolean> donateToCreater(CreateDonationDTO dto, User user) {
        if(dto.getCoin() < 1000 || dto.getCoin() % 1000 != 0){
            LOGGER.error(messageService.getMessage("donation.log.invalid-coin"),user.getId(),dto.getCoin());
            throw new InvalidDonateCoinException(messageService.getMessage("donation.invalid-coin"));
        }

        if(user.getCoin() < dto.getCoin()){
            LOGGER.error(messageService.getMessage("donation.log.not-enough"),user.getId(),dto.getCoin());
            throw new NotEnoughCoinException(messageService.getMessage("donation.not-enough"));
        }

        var bookFound = bookService.findById(dto.getBookId()).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"), dto.getBookId());
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        var receiverFound = userService.findById(dto.getReceiveId()).orElse(null);
        if (receiverFound == null) {
            LOGGER.error(messageService.getMessage("user.log.not-found"), dto.getReceiveId());
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }

        Donation donation = Donation.builder()
                .book(bookFound)
                .createdAt(LocalDateTime.now())
                .coin(dto.getCoin())
                .userGave(user)
                .userReceived(receiverFound)
                .build();

        donationRepository.save(donation);
        LOGGER.info(messageService.getMessage("donation.log.success"),user.getId(),dto.getCoin(),receiverFound.getId());

        redisService.evictCachePrefixAndSuffixUserId(CACHE_VALUE,receiverFound.getId());

        return new ResponseSuccess<>(messageService.getMessage("donation.success"),true);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'bookId:' + #bookId + ',pageIndex:' + pageIndex + ',userId:' + #user.id")
    public ResponseSuccess<ResponsePaging<List<DonationVm>>> getAllDonationForBook(int bookId,int pageIndex, User user) {
        var bookFound = bookService.findById(bookId).orElse(null);
        if (bookFound == null) {
            LOGGER.error(messageService.getMessage("book.log.not-found"), bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        if(!bookFound.getUser().equals(user)){

            LOGGER.error(messageService.getMessage("book.log.not-the-creater"),user.getId(),bookId);
            throw new NotCreaterOfBookException(messageService.getMessage("book.not-the-creater"));
        }

        Pageable paging = PageRequest.of(pageIndex,DONATION_PAGE_SIZE, Sort.by(Sort.Direction.DESC,DONATION_SORT_BY));

        Page<Donation> pagingResult = donationRepository.getAllDonationForBook(paging, bookFound);

        var data = pagingResult.getContent().stream().map(mappingService::getDonationVm).toList();

        ResponsePaging<List<DonationVm>> result = ResponsePaging.<List<DonationVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(DONATION_PAGE_SIZE)
                .totalRecord(pagingResult.getTotalElements())
                .totalPage(pagingResult.getTotalPages())
                .data(data)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }
}
