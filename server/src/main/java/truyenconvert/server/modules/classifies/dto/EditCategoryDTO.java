package truyenconvert.server.modules.classifies.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryDTO {
    @NotNull(message = "Trường title {not.null.message}")
    @NotBlank(message = "Trường title {blank.message}")
    @Length(min = 1,max = 255,message = "Trường title {length.message}")
    private String title;

    @NotNull(message = "Trường description {not.null.message}")
    @NotBlank(message = "Trường description {blank.message}")
    @Length(min = 1,max = 255,message = "Trường description {length.message}")
    private String description;
}
