package truyenconvert.server.modules.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.ReportType;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType,Integer> {

}
