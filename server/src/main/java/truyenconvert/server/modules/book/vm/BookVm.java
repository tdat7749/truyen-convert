package truyenconvert.server.modules.book.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;
import truyenconvert.server.modules.classifies.vm.CategoryVm;
import truyenconvert.server.modules.classifies.vm.SectVm;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;
import truyenconvert.server.modules.users.vm.UserVm;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class BookVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String introduction;
    private PosterVm posters;
    private SectVm sect;
    private CategoryVm category;
    @JsonProperty("world_context")
    private WorldContextVm worldContext;
    @JsonProperty("count_view")
    private long view;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("original_link")
    private String originalLink;
    private AuthorVm author;
    private UserVm creater;
    @JsonProperty("is_vip")
    private boolean isVip;
    @JsonProperty("is_delete")
    private boolean isDelete;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private double score;
    @JsonProperty("new_chap_at")
    private String newChapAt;
    private String slug;

    @JsonProperty("status_name")
    private String statusName;
    @JsonProperty("state_name")
    private String stateName;
    private int status;
    private int state;

    @JsonProperty("count_chapter")
    private long countChapter;
    @JsonProperty("count_word")
    private long countWord;
    @JsonProperty("count_comment")
    private long countComment;
    @JsonProperty("count_review")
    private long countEvaluation;
}
