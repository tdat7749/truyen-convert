package truyenconvert.server.modules.notification.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import truyenconvert.server.models.enums.NotificationType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotiDTO {

    @JsonProperty("sender_id")
    private Integer senderId;

    @NotNull(message = "Trường type {not.null.message}")
    @NotBlank(message = "Trường type {blank.message}")
    private NotificationType type;

    @NotNull(message = "Trường message {not.null.message}")
    @NotBlank(message = "Trường message {blank.message}")
    @Length(min = 1,max = 1000,message = "Trường message {length.message}")
    private String message;
}
