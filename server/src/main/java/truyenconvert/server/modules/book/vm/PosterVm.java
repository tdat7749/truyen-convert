package truyenconvert.server.modules.book.vm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class PosterVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String xDefault;
    private String x150;
    private String x300;
    private String x600;
}
