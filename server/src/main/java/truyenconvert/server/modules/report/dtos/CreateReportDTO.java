package truyenconvert.server.modules.report.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportDTO {
    @NotNull(message = "Trường content {not.null.message}")
    @NotBlank(message = "Trường content {blank.message}")
    @Length(min = 1,max = 1000,message = "Trường content {length.message}")
    private String content;

    @JsonProperty("report_type")
    @NotNull(message = "Trường report_type {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường report_type {digits.message}")
    private int reportTypeId;
}
