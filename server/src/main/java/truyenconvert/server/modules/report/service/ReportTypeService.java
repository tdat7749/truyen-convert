package truyenconvert.server.modules.report.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportTypeDTO;
import truyenconvert.server.modules.report.dtos.EditReportTypeDTO;
import truyenconvert.server.modules.report.vm.ReportTypeVm;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportTypeService {
    ResponseSuccess<ReportTypeVm> createReportType(CreateReportTypeDTO dto, User user);
    ResponseSuccess<List<ReportTypeVm>> getAllReportType();
    ResponseSuccess<ReportTypeVm> editReportType(EditReportTypeDTO dto,int id, User user);
    Optional<ReportType> findById(int id);
    List<ReportType> getAll(Sort.Direction sort,String field);

    ResponseSuccess<ReportTypeVm> getReportTypeById(int id); // for admin
}
