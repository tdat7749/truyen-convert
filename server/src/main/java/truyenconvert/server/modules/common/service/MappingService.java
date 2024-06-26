package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;

@Service
public interface MappingService {
    ReportTypeVm getReportTypeVm(ReportType reportType);
    ReportVm getReportVm(ReportVm reportVm);
}
