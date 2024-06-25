package truyenconvert.server.modules.report.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.report.dtos.CreateReportTypeDTO;
import truyenconvert.server.modules.report.dtos.EditReportTypeDTO;
import truyenconvert.server.modules.report.vm.ReportTypeVm;

@Service
public interface ReportTypeService {
    ResponseSuccess<ReportTypeVm> createReportType(CreateReportTypeDTO dto);
    ResponseSuccess<ReportTypeVm> getAllReportType();
    ResponseSuccess<ReportTypeVm> editReportType(EditReportTypeDTO dto);
}
