package truyenconvert.server.modules.book.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChapterDetailVm {
    private int id;
    private String title;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String content;
    @JsonProperty("time_expired")
    @Nullable
    private String timeExpired;
    @JsonProperty("unlock_coin")
    private long unLockCoin;
    private int chapter;
    @JsonProperty("previous_chapter")
    private String previousChapter;
    @JsonProperty("next_chapter")
    private String nextChapter;
    @JsonProperty("total_chapter")
    private int totalChapter;
    @JsonProperty("is_unlock")
    private boolean isUnlock;
}
