package truyenconvert.server.modules.report.vm;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportTypeVm {
    private int id;
    private String title;
    private String description;
    @Nullable
    private String note;
}
