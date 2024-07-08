package truyenconvert.server.modules.users.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVm {
    private int id;
    @JsonProperty("display_name")
    private String displayName;
    private String avatar;
    private int level;
    private long exp;
}
