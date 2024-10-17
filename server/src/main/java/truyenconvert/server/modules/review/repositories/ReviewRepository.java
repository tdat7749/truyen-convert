package truyenconvert.server.modules.review.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query(value = "SELECT CASE WHEN count(r) > 0 THEN true ELSE false END FROM reviews as r WHERE r.book_id =:bookId AND r.user_id =:userId",nativeQuery = true)
    boolean isReviewed(int bookId, int userId);

    @Query(value = "SELECT r.score FROM Review as r WHERE r.book =:book")
    List<Integer> getListScoreReviewByBook(Book book);

    @Query(value = "SELECT r FROM Review as r WHERE r.book =:book")
    Page<Review> getAllReviews(Pageable paging, Book book);
}
