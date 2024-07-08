package truyenconvert.server.modules.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {
    @NotNull(message = "Trường email {not.null.message}")
    @NotBlank(message = "Trường email {blank.message}")
    private String email;

    @NotNull(message = "Trường password {not.null.message}")
    @NotBlank(message = "Trường password {blank.message}")
    private String password;
}
