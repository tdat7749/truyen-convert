package truyenconvert.server.modules.read_histories.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.ReadHistory;
import truyenconvert.server.models.User;

import java.util.Optional;

public interface ReadHistoryRepository extends JpaRepository<ReadHistory, Integer> {
    Optional<ReadHistory> findByBookAndUser(Book book, User user);

    @Query("SELECT rh FROM ReadHistory AS rh LEFT JOIN rh.book as b WHERE b.isDeleted = false AND rh.user =:user")
    Page<ReadHistory> getAllReadHistoryByUser(Pageable paging, User user);
}
