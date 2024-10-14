package truyenconvert.server.modules.read_histories.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import truyenconvert.server.modules.book.vm.BookSimpleVm;
import truyenconvert.server.modules.book.vm.BookVm;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadHistoryVm {
    private BookSimpleVm book;
    private int chapter;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}
