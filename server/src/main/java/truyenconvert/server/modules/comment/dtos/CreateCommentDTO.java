package truyenconvert.server.modules.comment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {

    @JsonProperty("parent_id")
    @Positive(message = "Trường parent_id {positive.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường parent_id {digits.message}")
    private Integer parentId;

    @NotNull(message = "Trường content {not.null.message}")
    @NotBlank(message = "Trường content {blank.message}")
    @Length(min = 1,max = 100,message = "Trường content {length.message}")
    private String content;

    @JsonProperty("book_id")
    @Positive(message = "Trường book_id {positive.message}")
    @NotNull(message = "Trường book_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường book_id {digits.message}")
    private int bookId;
}
