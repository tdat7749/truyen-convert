package truyenconvert.server.modules.read_histories.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import truyenconvert.server.models.ReadHistory;

public interface ReadHistoryRepository extends JpaRepository<ReadHistory, Integer> {

}
