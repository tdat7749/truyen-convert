package truyenconvert.server.modules.book.vm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PosterVm {
    private String xDefault;
    private String x150;
    private String x300;
    private String x600;
}
