package truyenconvert.server.modules.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import truyenconvert.server.modules.book.model.Author;

public interface AuthorRepository extends JpaRepository<Author,Integer> {
}
