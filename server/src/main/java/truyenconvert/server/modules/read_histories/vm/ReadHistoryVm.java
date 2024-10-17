package truyenconvert.server.modules.read_histories.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import truyenconvert.server.modules.book.vm.BookSimpleVm;
import truyenconvert.server.modules.book.vm.BookVm;

import java.io.Serial;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadHistoryVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BookSimpleVm book;
    private int chapter;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}
