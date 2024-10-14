package truyenconvert.server.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePaging<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("page_index")
    private int pageIndex;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("total_page")
    private int totalPage;
    @JsonProperty("total_record")
    private long totalRecord;
    private T data;
}
