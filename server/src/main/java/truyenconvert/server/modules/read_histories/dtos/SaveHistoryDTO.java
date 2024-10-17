package truyenconvert.server.modules.read_histories.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveHistoryDTO {
    @JsonProperty("book_id")
    @Positive(message = "Trường book_id {positive.message}")
    @NotNull(message = "Trường book_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường book_id {digits.message}")
    private int bookId;

    @Positive(message = "Trường chapter {positive.message}")
    @NotNull(message = "Trường chapter {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường chapter {digits.message}")
    private int chapter;
}
