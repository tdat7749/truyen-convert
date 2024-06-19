package truyenconvert.server.modules.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
public class ChangePasswordDTO {
    @JsonProperty("current_password")
    @NotNull(message = "Trường current_password {not.null.message}")
    @NotBlank(message = "Trường current_password {blank.message}")
    private String currentPassword;

    @JsonProperty("new_password")
    @NotNull(message = "Trường new_password {not.null.message}")
    @NotBlank(message = "Trường new_password {blank.message}")
    @Length(min = 1,max = 200,message = "Trường new_password {length.message}")
    private String newPassword;

    @JsonProperty("confirm_new_password")
    @NotNull(message = "Trường confirm_new_password {not.null.message}")
    @NotBlank(message = "Trường confirm_new_password {blank.message}")
    @Length(min = 1,max = 200,message = "Trường confirm_new_password {length.message}")
    private String confirmNewPassword;
}
