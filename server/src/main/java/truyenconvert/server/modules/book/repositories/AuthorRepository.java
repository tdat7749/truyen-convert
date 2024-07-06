package truyenconvert.server.modules.book.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Integer> {
    Optional<Author> findByAuthorNameOrOriginalAuthorName(String authorName,String orginalAuthorName);


    @Query("Select a from Author as a where a.authorName LIKE %:keyword% OR a.originalAuthorName LIKE %:keyword%")
    Page<Author> getAllAuthor(String keyword, Pageable paging);
}
