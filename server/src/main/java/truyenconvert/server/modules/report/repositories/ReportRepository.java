package truyenconvert.server.modules.report.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import truyenconvert.server.models.Report;
import truyenconvert.server.models.User;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT r from Report as r where r.user =: user and r.status =: status")
    Page<Report> getAllReportForUser(User user, int status, Pageable paging);

    @Query("SELECT r from Report as r where r.status =: status")
    Page<Report> getAllReportForAdmin(int status, Pageable paging);
}
