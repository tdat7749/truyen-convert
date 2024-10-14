package truyenconvert.server.modules.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.report.dtos.CreateReportTypeDTO;
import truyenconvert.server.modules.report.dtos.EditReportTypeDTO;
import truyenconvert.server.modules.report.exceptions.ReportTypeNotFoundException;
import truyenconvert.server.modules.report.repositories.ReportTypeRepository;
import truyenconvert.server.modules.report.vm.ReportTypeVm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportTypeServiceImpl implements ReportTypeService{
    private final ReportTypeRepository reportTypeRepository;
    private final MappingService mappingService;
    private final MessageService messageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTypeServiceImpl.class);

    public ReportTypeServiceImpl(
            ReportTypeRepository reportTypeRepository,
            MappingService mappingService,
            MessageService messageService
    ){
        this.reportTypeRepository = reportTypeRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
    }

    @Override
    @CacheEvict(value = "reportTypes", allEntries = true)
    public ResponseSuccess<ReportTypeVm> createReportType(CreateReportTypeDTO dto, User user) {
        ReportType reportType = ReportType.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .note(dto.getNote() != null ? dto.getNote() : null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var save = reportTypeRepository.save(reportType);

        LOGGER.info(messageService.getMessage("report-type.log.create.success"),user.getId(), save.getId());

        return new ResponseSuccess<>(
                messageService.getMessage("report-type.create.success")
                ,mappingService.getReportTypeVm(reportType)
        );
    }

    @Override
    @Cacheable(value = "reportTypes")
    public ResponseSuccess<List<ReportTypeVm>> getAllReportType() {
        var listReportType = this.getAll(Sort.Direction.ASC,"title");
        var result = listReportType.stream().map(mappingService::getReportTypeVm).toList();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    @CacheEvict(value = "reportTypes", allEntries = true)
    public ResponseSuccess<ReportTypeVm> editReportType(EditReportTypeDTO dto,int id, User user) {
        var reportTypeFound = this.findById(id).orElse(null);
        if(reportTypeFound == null){
            LOGGER.error(messageService.getMessage("report-type.log.not-found"), id);
            throw new ReportTypeNotFoundException(messageService.getMessage("report-type.not-found"));
        }

        reportTypeFound.setTitle(dto.getTitle());
        reportTypeFound.setDescription(dto.getDescription());
        reportTypeFound.setNote(dto.getNote());
        reportTypeFound.setUpdatedAt(LocalDateTime.now());

        LOGGER.info(messageService.getMessage("report-type.log.edit.success"),user.getId(),reportTypeFound.getId());

        return new ResponseSuccess<>(
                messageService.getMessage("report-type.edit.success")
                ,mappingService.getReportTypeVm(reportTypeFound)
        );
    }

    @Override
    public Optional<ReportType> findById(int id) {
        return reportTypeRepository.findById(id);
    }

    @Override
    public List<ReportType> getAll(Sort.Direction sort,String field) {
        return reportTypeRepository.findAll(Sort.by(sort,field));
    }

    @Override
    @Cacheable(value = "reportTypes", key = "#id")
    public ResponseSuccess<ReportTypeVm> getReportTypeById(int id) {
        var reportTypeFound = this.findById(id).orElse(null);
        if(reportTypeFound == null){
            LOGGER.error(messageService.getMessage("report-type.log.not-found"), id);
            throw new ReportTypeNotFoundException(messageService.getMessage("report-type.not-found"));
        }

        ReportTypeVm reportTypeVm = mappingService.getReportTypeVm(reportTypeFound);

        return new ResponseSuccess<>("Thành công.",reportTypeVm);
    }
}
