package truyenconvert.server.modules.classifies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import truyenconvert.server.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
