package truyenconvert.server.modules.report.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportDTO;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.util.List;

@Service
public interface ReportService {
    ResponseSuccess<ReportVm> createReport(CreateReportDTO dto, User user);
    ResponseSuccess<ReportVm> handleReport(int id, User user);
    ResponseSuccess<List<ReportVm>> getAllReportForAdmin(int pageIndex, int reportStatus);
    ResponseSuccess<List<ReportVm>> getAllReportForUser(int pageIndex);
}
