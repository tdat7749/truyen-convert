package truyenconvert.server.modules.report.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Report;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportDTO;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportService {
    ResponseSuccess<ReportVm> createReport(CreateReportDTO dto, User user);
    ResponseSuccess<ReportVm> handleReport(int id, User user);
    ResponseSuccess<ResponsePaging<List<ReportVm>>> getAllReportForAdmin(int pageIndex, int reportStatus,String sort);
    ResponseSuccess<ResponsePaging<List<ReportVm>>> getAllReportForUser(int pageIndex,int reportStatus,String sort,User user);
    Optional<Report> findById(int id);
    ResponseSuccess<ReportVm> getReportById(int id); // for admin
}
