package truyenconvert.server.modules.book.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.*;

@Builder
@Getter
@Setter
public class ChapterVm {
    private int id;
    private String title;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private int chapter;
    @JsonProperty("unlock_coin")
    private long unLockCoin;
    @JsonProperty("time_expired")
    @Nullable
    private String timeExpired;

}
