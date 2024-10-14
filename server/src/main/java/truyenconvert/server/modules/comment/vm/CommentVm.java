package truyenconvert.server.modules.comment.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String content;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("total_like")
    private long totalLike;
    @JsonProperty("is_liked")
    private boolean isLiked;
    @JsonProperty("total_reply")
    private long totalReply;

    public CommentVm(int id,String content,String updatedAt,String createdAt, long totalLike, boolean isLiked){
        this.id = id;
        this.content = content;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.totalLike = totalLike;
        this.isLiked = isLiked;
    }
}
