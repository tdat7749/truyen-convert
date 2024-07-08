package truyenconvert.server.modules.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<Book> findBySlug(String slug);
    Optional<Book> findByOriginalNameOrOriginalLink(String originalName, String originalLink);

    @Query("SELECT count(c) from Chapter as c where c.book =:book")
    Long getTotalChapterOfBook(Book book);

    @Query("SELECT count(c) from Comment as c where c.book =:book")
    Long getTotalCommentOfBook(Book book);

    @Query("SELECT count(e) from Evaluation  as e where e.book =:book")
    Long getTotalReviewOfBook(Book book);

    @Query("SELECT sum(c.wordCount)  from Chapter  as c where c.book =:book")
    Long getTotalWordOfBook(Book book);

    @Query("SELECT sum(c.viewCount) from Chapter as c where c.book =:book")
    Long getTotalViewOfBook(Book book);
}
