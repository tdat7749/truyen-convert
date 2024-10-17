package truyenconvert.server.modules.book.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Chapter;
import truyenconvert.server.models.User;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {

    @Query("Select c.chapter FROM Chapter as c where c.book =:book order by c.chapter desc limit 1")
    Integer getNewestChaperOfBook(Book book);

    @Query("SELECT c FROM Chapter as c where c.book =: book")
    Page<Chapter> getAllChapterOfBook(Book book, Pageable paging);

    @Query(value = "SELECT CASE WHEN count(uc) > 0 THEN TRUE ELSE FALSE END from unlock_chapter as uc where uc.user_id =:userId AND uc.chapter_id =:chapterId",nativeQuery = true)
    Boolean existsByUserAndChapter(@Param("userId") int userId,@Param("chapterId") int chapterId);

    @Query("SELECT c FROM Chapter as c where c.book =:book and c.chapter =:chapter")
    Optional<Chapter> findByBookAndChapter(Book book, int chapter);
}
