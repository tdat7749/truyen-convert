package truyenconvert.server.modules.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import truyenconvert.server.modules.book.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {

}
