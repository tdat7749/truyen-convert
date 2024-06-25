package truyenconvert.server.modules.report.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportDTO;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{
    @Override
    public ResponseSuccess<ReportVm> createReport(CreateReportDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<ReportVm> handleReport(int id, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<ReportVm>> getAllReportForAdmin(int pageIndex, int reportStatus) {
        return null;
    }

    @Override
    public ResponseSuccess<List<ReportVm>> getAllReportForUser(int pageIndex) {
        return null;
    }
}
