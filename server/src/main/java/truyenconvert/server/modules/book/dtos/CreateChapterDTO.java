package truyenconvert.server.modules.book.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChapterDTO {

    @NotNull(message = "Trường title {not.null.message}")
    @NotBlank(message = "Trường title {blank.message}")
    @Length(min = 1,max = 255,message = "Trường title {length.message}")
    private String title;

    @NotNull(message = "Trường content {not.null.message}")
    @NotBlank(message = "Trường content {blank.message}")
    private String content;

    @JsonProperty("unlock_coin")
    @Positive(message = "Trường unlock_coin {positive.message}")
    @NotNull(message = "Trường unlock_coin {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường unlock_coin {digits.message}")
    private long unLockCoin;

    @JsonProperty("time_expired")
    @Future(message = "Thời gian của time_expired {future.message}")
    private LocalDateTime timeExpired;

    @JsonProperty("book_id")
    @Digits(integer = 9, fraction = 0, message = "Trường bookId {digits.message}")
    @NotNull(message = "Trường bookId {not.null.message}")
    private int bookId;
}
