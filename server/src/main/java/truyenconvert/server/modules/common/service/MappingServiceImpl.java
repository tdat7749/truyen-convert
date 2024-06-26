package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;

@Service
public class MappingServiceImpl implements MappingService{
    @Override
    public ReportTypeVm getReportTypeVm(ReportType reportType) {
        return ReportTypeVm.builder()
                .title(reportType.getTitle())
                .description(reportType.getDescription())
                .note(reportType.getNote() != null ? reportType.getNote() : null)
                .build();
    }

    @Override
    public ReportVm getReportVm(ReportVm reportVm) {
        return null;
    }
}
