package truyenconvert.server.modules.report.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.users.vm.UserVm;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ReportVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String content;
    private String createdAt;
    private String updatedAt;
    @Nullable
    private UserVm handler;
    private UserVm user;
    @JsonProperty("report_status")
    private String reportStatus;
}
