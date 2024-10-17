package truyenconvert.server.modules.users.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UserVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    @JsonProperty("display_name")
    private String displayName;
    private String avatar;
    private int level;
    private long exp;
}
