package truyenconvert.server.modules.notification.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String message;
    private NotificationType type;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private User sender;
    @JsonProperty("is_read")
    private boolean isRead;
    @JsonProperty("time_read")
    private String timeRead;
}
