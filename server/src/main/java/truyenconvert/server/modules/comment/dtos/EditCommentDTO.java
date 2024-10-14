package truyenconvert.server.modules.comment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentDTO {
    private int id;

    @NotNull(message = "Trường content {not.null.message}")
    @NotBlank(message = "Trường content {blank.message}")
    @Length(min = 1,max = 100,message = "Trường content {length.message}")
    private String content;
}
