package truyenconvert.server.modules.review.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ReviewVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String content;
    private float score;
    @JsonProperty("created_at")
    private String createdAt;
}
