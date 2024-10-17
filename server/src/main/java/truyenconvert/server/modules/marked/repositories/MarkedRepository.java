package truyenconvert.server.modules.marked.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Marked;

@Repository
public interface MarkedRepository extends JpaRepository<Marked, Integer> {

}
