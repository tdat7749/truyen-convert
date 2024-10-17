package truyenconvert.server.modules.donation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDonationDTO {

    @Positive(message = "Trường coin {positive.message}")
    @NotNull(message = "Trường coin {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường coin {digits.message}")
    private int coin;

    @JsonProperty("book_id")
    @Positive(message = "Trường book_id {positive.message}")
    @NotNull(message = "Trường book_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường book_id {digits.message}")
    private int bookId;

    @JsonProperty("receive_id")
    @Positive(message = "Trường receive_id {positive.message}")
    @NotNull(message = "Trường receive_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường receive_id {digits.message}")
    private int receiveId;

    @JsonProperty("give_id")
    @Positive(message = "Trường give_id {positive.message}")
    @NotNull(message = "Trường give_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường give_id {digits.message}")
    private int giveId;
}
