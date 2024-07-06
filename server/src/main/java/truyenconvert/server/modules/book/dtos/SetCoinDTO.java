package truyenconvert.server.modules.book.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetCoinDTO {
    @Positive(message = "Trường coin {positive.message}")
    @NotNull(message = "Trường coin {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường coin {digits.message}")
    private long coin;

    @JsonProperty("time_expired")
    @Future(message = "Thời gian của time_expired {future.message}")
    private LocalDateTime timeExpired;
}
