package truyenconvert.server.modules.scheduling_task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import truyenconvert.server.models.Book;
import truyenconvert.server.modules.book.repositories.BookRepository;

import java.util.List;

@Component
public class ScheduledTasks {
    private final BookRepository bookRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static final int PAGE_SIZE = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);


    public ScheduledTasks(
            BookRepository bookRepository,
            ThreadPoolTaskScheduler threadPoolTaskScheduler
    ){
        this.bookRepository = bookRepository;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

//    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateBooks(){
        LOGGER.info("Bắt đầu cập nhật bằng scheduling");
        Pageable paging = PageRequest.of(0,PAGE_SIZE);
        Page<Book> bookPage;

        do {
            bookPage = bookRepository.findAll(paging);
            List<Book> books = bookPage.getContent();

            books.forEach(book -> threadPoolTaskScheduler.execute(() -> updateBook(book)));

            paging = paging.next();
        }while (bookPage.hasNext());
    }

    @Transactional
    public void updateBook(Book book){
        LOGGER.info("Cập nhật cho book với ID = {}",book.getId());
        Long reviewCount = bookRepository.getTotalReviewOfBook(book);
        Long commentCount = bookRepository.getTotalCommentOfBook(book);
        Long wordCount = bookRepository.getTotalWordOfBook(book);
        Long viewCount = bookRepository.getTotalViewOfBook(book);

        book.setCountComment(commentCount != null ? commentCount : 0);
        book.setCountWord(wordCount != null ? wordCount : 0);
        book.setCountEvaluation(reviewCount != null ? reviewCount : 0);
        book.setView(viewCount != null ? viewCount : 0);

        bookRepository.save(book);

        LOGGER.info("Cập nhật hoàn thành cho book với ID = {}",book.getId());
    }

}
