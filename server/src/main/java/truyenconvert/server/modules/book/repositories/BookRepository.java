package truyenconvert.server.modules.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import truyenconvert.server.modules.book.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

}
