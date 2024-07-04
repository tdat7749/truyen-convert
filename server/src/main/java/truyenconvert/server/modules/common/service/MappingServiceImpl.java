package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.Report;
import truyenconvert.server.models.ReportType;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;
import truyenconvert.server.modules.users.vm.UserVm;

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
    public ReportVm getReportVm(Report report) {
        return ReportVm.builder()
                .createdAt(report.getCreatedAt().toString())
                .updatedAt(report.getUpdatedAt().toString())
                .content(report.getContent())
                .user(this.getUserVm(report.getUser()))
                .handler(report.getUser() != null ? this.getUserVm(report.getUser()) : null)
                .reportStatus(report.getStatus().name())
                .build();
    }

    @Override
    public UserVm getUserVm(User user) {
        return UserVm.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }
}
