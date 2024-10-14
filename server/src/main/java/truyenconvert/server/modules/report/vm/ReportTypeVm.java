package truyenconvert.server.modules.report.vm;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ReportTypeVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String description;
    @Nullable
    private String note;
}
