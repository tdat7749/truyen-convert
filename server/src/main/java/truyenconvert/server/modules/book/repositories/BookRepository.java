package truyenconvert.server.modules.book.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Category;
import truyenconvert.server.models.Sect;
import truyenconvert.server.models.WorldContext;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<Book> findBySlug(String slug);
    Optional<Book> findByOriginalNameOrOriginalLink(String originalName, String originalLink);

    @Query("SELECT count(c) from Chapter as c where c.book =:book")
    Long getTotalChapterOfBook(Book book);

    @Query("SELECT count(c) from Comment as c where c.book =:book")
    Long getTotalCommentOfBook(Book book);

    @Query("SELECT count(e) from Review  as e where e.book =:book")
    Long getTotalReviewOfBook(Book book);

    @Query("SELECT sum(c.wordCount)  from Chapter  as c where c.book =:book")
    Long getTotalWordOfBook(Book book);

    @Query("SELECT sum(c.viewCount) from Chapter as c where c.book =:book")
    Long getTotalViewOfBook(Book book);

    @Query("SELECT b from Book as b inner join b.author as a " +
            "WHERE (:world IS NULL OR b.worldContext =:world)" +
            "AND (:sect IS NULL OR b.sect =:sect)" +
            "AND (:cate IS NULL OR b.category =:cate)" +
            "AND (:isVip IS NULL OR b.isVip =:isVip)" +
            "AND (b.state =:state)" +
            "AND b.title LIKE %:keyword% OR b.originalName LIKE %:keyword% OR a.authorName LIKE %:keyword% OR a.originalAuthorName LIKE %:keyword% "
    )
    Page<Book> getAllPublicBook(Pageable paging, String keyword, WorldContext world, Sect sect, Category cate, BookState state, Integer isVip);

    @Query("SELECT b from Book as b inner join b.author as a " +
            "WHERE (:world IS NULL OR b.worldContext =:world)" +
            "AND (:sect IS NULL OR b.sect =:sect)" +
            "AND (:cate IS NULL OR b.category =:cate)" +
            "AND (:isVip IS NULL OR b.isVip =:isVip)" +
            "AND (b.state =:state)" +
            "AND (b.status =:status)" +
            "AND b.title LIKE %:keyword% OR b.originalName LIKE %:keyword% OR a.authorName LIKE %:keyword% OR a.originalAuthorName LIKE %:keyword% "
    )
    Page<Book> getAllBook(Pageable paging, String keyword, WorldContext world, Sect sect, Category cate, BookStatus status ,BookState state, Integer isVip);
}
