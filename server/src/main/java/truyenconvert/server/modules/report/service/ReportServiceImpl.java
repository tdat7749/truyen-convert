package truyenconvert.server.modules.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Report;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.ReportStatus;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.report.dtos.CreateReportDTO;
import truyenconvert.server.modules.report.exceptions.ReportNotFoundException;
import truyenconvert.server.modules.report.exceptions.ReportTypeNotFoundException;
import truyenconvert.server.modules.report.repositories.ReportRepository;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService{

    @Value("${truyencv.page-size}")
    private int pageSize;
    private final ReportRepository reportRepository;

    private final ReportTypeService reportTypeService;
    private final MessageService messageService;
    private final MappingService mappingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(
            ReportTypeService reportTypeService,
            MessageService messageService,
            ReportRepository reportRepository,
            MappingService mappingService
    ){
        this.reportTypeService = reportTypeService;
        this.messageService = messageService;
        this.reportRepository = reportRepository;
        this.mappingService = mappingService;
    }

    @Override
    public ResponseSuccess<ReportVm> createReport(CreateReportDTO dto, User user) {
        var reportTypeFound = reportTypeService.findById(dto.getReportTypeId()).orElse(null);
        if(reportTypeFound == null){
            throw new ReportTypeNotFoundException(messageService.getMessage("report-type.not-found"));
        }

        Report report = Report.builder()
                .reportType(reportTypeFound)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .content(dto.getContent())
                .user(user)
                .build();

        var save =  reportRepository.save(report);

        LOGGER.info("{} {} tạo một báo cáo.",user.getRole().toString(),user.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("report.create.success"), mappingService.getReportVm(save));
    }

    @Override
    public ResponseSuccess<ReportVm> handleReport(int id, User user) {
        var reportFound = this.findById(id).orElse(null);
        if(reportFound == null){
            throw new ReportNotFoundException(messageService.getMessage("report.not-found"));
        }

        reportFound.setStatus(ReportStatus.Handled);

        var save = reportRepository.save(reportFound);

        LOGGER.info("{} {} xử lý báo cáo với ID = {}.",user.getRole().toString(),user.getEmail(),reportFound.getId());

        return new ResponseSuccess<>(messageService.getMessage("report.handled.success"), mappingService.getReportVm(save));
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<ReportVm>>> getAllReportForAdmin(int pageIndex, int reportStatus, String sort) {
        var sortBy = Sort.by(Sort.Direction.DESC,"createdAt");
        if (sort.equals("updated_at")){
            sortBy = Sort.by(Sort.Direction.DESC,"updatedAt");
        }else if(sort.equals("oldest")){
            sortBy = Sort.by(Sort.Direction.ASC,"createdAt");
        }

        Pageable paging = PageRequest.of(pageIndex,pageSize, sortBy);

        var pagingResult = reportRepository.getAllReportForAdmin(reportStatus,paging);

        List<ReportVm> reportVmList = pagingResult.stream().map(mappingService::getReportVm).toList();

        ResponsePaging<List<ReportVm>> result = ResponsePaging.<List<ReportVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(reportVmList)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<ReportVm>>> getAllReportForUser(int pageIndex,int reportStatus,String sort,User user) {
        var sortBy = Sort.by(Sort.Direction.DESC,"createdAt");
        if (sort.equals("updated_at")){
            sortBy = Sort.by(Sort.Direction.DESC,"updatedAt");
        }else if(sort.equals("oldest")){
            sortBy = Sort.by(Sort.Direction.ASC,"createdAt");
        }

        Pageable paging = PageRequest.of(pageIndex,pageSize, sortBy);

        var pagingResult = reportRepository.getAllReportForUser(user,reportStatus,paging);

        List<ReportVm> reportVmList = pagingResult.stream().map(mappingService::getReportVm).toList();

        ResponsePaging<List<ReportVm>> result = ResponsePaging.<List<ReportVm>>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(reportVmList)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    public Optional<Report> findById(int id) {
        return reportRepository.findById(id);
    }

    @Override
    public ResponseSuccess<ReportVm> getReportById(int id) {
        var reportFound = this.findById(id).orElse(null);
        if(reportFound == null){
            throw new ReportNotFoundException(messageService.getMessage("report.not-found"));
        }

        ReportVm reportVm = mappingService.getReportVm(reportFound);

        return new ResponseSuccess<>("Thành công.",reportVm);
    }
}
