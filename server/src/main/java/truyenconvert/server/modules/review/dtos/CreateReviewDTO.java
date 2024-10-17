package truyenconvert.server.modules.review.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDTO {

    @NotNull(message = "Trường content {not.null.message}")
    @NotBlank(message = "Trường content {blank.message}")
    @Length(min = 1,max = 1000,message = "Trường content {length.message}")
    private String content;

    @Positive(message = "Trường score {positive.message}")
    @NotNull(message = "Trường score {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường score {digits.message}")
    private int score;
}
