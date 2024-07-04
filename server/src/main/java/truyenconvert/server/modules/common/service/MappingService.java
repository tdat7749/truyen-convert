package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.Report;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;
import truyenconvert.server.modules.users.vm.UserVm;

@Service
public interface MappingService {
    ReportTypeVm getReportTypeVm(ReportType reportType);
    ReportVm getReportVm(Report report);
    UserVm getUserVm(User user);
}
