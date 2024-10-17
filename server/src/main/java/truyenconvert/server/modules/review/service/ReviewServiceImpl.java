package truyenconvert.server.modules.review.service;

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
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Review;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.modules.book.exceptions.BookNotFoundException;
import truyenconvert.server.modules.book.service.BookService;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.review.dtos.CreateReviewDTO;
import truyenconvert.server.modules.review.exceptions.InvalidReviewScoreException;
import truyenconvert.server.modules.review.exceptions.NotPermissionReviewException;
import truyenconvert.server.modules.review.exceptions.ReviewNotFoundException;
import truyenconvert.server.modules.review.exceptions.WasReviewedException;
import truyenconvert.server.modules.review.repositories.ReviewRepository;
import truyenconvert.server.modules.review.vm.ReviewVm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final String CACHE_VALUE = "reviews";
    private final int REVIEW_PAGE_SIZE = 5;
    private final String REVIEW_SORT_BY = "createdAt";
    private final int ROUND_PLACES = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final MappingService mappingService;
    private final MessageService messageService;
    private final BookService bookService;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            MappingService mappingService,
            MessageService messageService,
            BookService bookService
    ){
        this.reviewRepository = reviewRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<ReviewVm> createReview(CreateReviewDTO dto,int bookId, User user) {
        if(dto.getScore() > 5 || dto.getScore() < 1){
            LOGGER.error(messageService.getMessage("review.log.invalid-score"),user.getId(),bookId,dto.getScore());
            throw new InvalidReviewScoreException(messageService.getMessage("review.invalid-score"));
        }
        boolean isReviewed = reviewRepository.isReviewed(bookId,user.getId());
        if(isReviewed){
            LOGGER.error(messageService.getMessage("review.log.was-reviewed"),user.getId(),bookId);
            throw new WasReviewedException(messageService.getMessage("review.was-reviewed"));
        }

        var bookFound = bookService.findById(bookId).orElse(null);
        if(bookFound == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"),bookId);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        Review review = Review.builder()
                .createdAt(LocalDateTime.now())
                .score(dto.getScore())
                .content(dto.getContent())
                .book(bookFound)
                .user(user)
                .build();

        // get dữ liệu review của book từ db sau đó tính toán lại.
        List<Integer> reviewScores = reviewRepository.getListScoreReviewByBook(bookFound);
        reviewScores.add(dto.getScore());

        if(reviewScores.size() > 1){
            OptionalDouble average = reviewScores.stream()
                    .mapToInt(Integer::intValue)
                    .average();

            if(average.isPresent()){
                bookFound.setScore(round((float) average.getAsDouble(), ROUND_PLACES));
            }else{
                bookFound.setScore(round((float) 5,ROUND_PLACES));
            }
        }else{
            bookFound.setScore(round((float) dto.getScore(), ROUND_PLACES));
        }
        bookService.save(bookFound);
        var save = reviewRepository.save(review);

        LOGGER.info(messageService.getMessage("review.log.created.succes"),user.getId(),save.getId());
        var result = mappingService.getReviewVm(save);
        return new ResponseSuccess<>(messageService.getMessage("review.created.succes"),result);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'pageIndex:' + #pageIndex + ',slug:' + #slug")
    public ResponseSuccess<ResponsePaging<List<ReviewVm>>> getAllReviews(int pageIndex, String slug) {
        Pageable paging = PageRequest.of(REVIEW_PAGE_SIZE,pageIndex, Sort.by(Sort.Direction.DESC,REVIEW_SORT_BY));

        var bookFound = bookService.findBySlug(slug).orElse(null);
        if(bookFound == null){
            LOGGER.error(messageService.getMessage("book.log.not-found-slug"),slug);
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));

        }

        Page<Review> pagingResult = reviewRepository.getAllReviews(paging, bookFound);

        var data = pagingResult.getContent().stream().map(mappingService::getReviewVm).toList();

        ResponsePaging<List<ReviewVm>> result = ResponsePaging.<List<ReviewVm>>builder()
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .pageSize(REVIEW_PAGE_SIZE)
                .pageIndex(pageIndex)
                .data(data)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteReview(int id, User user) {
        var reviewFound = reviewRepository.findById(id).orElse(null);
        if(reviewFound == null){

            LOGGER.error(messageService.getMessage("review.log.not-found"),id);
            throw new ReviewNotFoundException(messageService.getMessage("review.not-found"));
        }

        Role userRole = user.getRole();
        if(!userRole.equals(Role.ADMIN) && !userRole.equals(Role.MODERATOR)){

            LOGGER.error(messageService.getMessage("review.log.not-permission"),user.getId(),id);
            throw new NotPermissionReviewException(messageService.getMessage("review.not-permission"));
        }
        Book book = reviewFound.getBook();

        reviewRepository.delete(reviewFound);

        List<Integer> reviewScores = reviewRepository.getListScoreReviewByBook(book);
        if(!reviewScores.isEmpty()){
            OptionalDouble average = reviewScores.stream()
                    .mapToInt(Integer::intValue)
                    .average();

            if(average.isPresent()){
                book.setScore(round((float) average.getAsDouble(), ROUND_PLACES));
            }else{
                book.setScore(round((float) 5, ROUND_PLACES));
            }
        }else{
            book.setScore(round((float) 5, ROUND_PLACES));
        }

        bookService.save(book);
        LOGGER.info(messageService.getMessage("review.log.deleted.success"),user.getId(),id);
        return new ResponseSuccess<>(messageService.getMessage("review.deleted.success"),true);
    }

    private float round(float value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
