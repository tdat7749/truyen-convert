package truyenconvert.server.modules.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
public class SignUpDTO {

    @NotNull(message = "Trường email {not.null.message}")
    @NotBlank(message = "Trường email {blank.message}")
    @Length(min = 1,max = 200,message = "Trường email {length.message}")
    @Email(message = "Trường email {email.message}")
    private String email;

    @NotNull(message = "Trường password {not.null.message}")
    @NotBlank(message = "Trường password {blank.message}")
    @Length(min = 1,max = 200,message = "Trường password {length.message}")
    private String password;

    @JsonProperty("confirm_password")
    @NotNull(message = "Trường confirm_password {not.null.message}")
    @NotBlank(message = "Trường confirm_password {blank.message}")
    @Length(min = 1,max = 200,message = "Trường confirm_password {length.message}")
    private String confirmPassword;

    @JsonProperty("display_name")
    @NotNull(message = "Trường display_name {not.null.message}")
    @NotBlank(message = "Trường display_name {blank.message}")
    @Length(min = 1,max = 200,message = "Trường display_name {length.message}")
    private String displayName;
}
