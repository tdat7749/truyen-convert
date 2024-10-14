package truyenconvert.server.modules.book.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditBookDTO {
    @NotNull(message = "Trường title {not.null.message}")
    @NotBlank(message = "Trường title {blank.message}")
    @Length(min = 1,max = 400,message = "Trường title {length.message}")
    private String title;

    @NotNull(message = "Trường slug {not.null.message}")
    @NotBlank(message = "Trường slug {blank.message}")
    @Length(min = 1,max = 430,message = "Trường slug {length.message}")
    private String slug;

    @JsonProperty("origianl_name")
    @NotNull(message = "Trường origianl_name {not.null.message}")
    @NotBlank(message = "Trường origianl_name {blank.message}")
    @Length(min = 1,max = 255,message = "Trường origianl_name {length.message}")
    private String originalName;

    @JsonProperty("original_link")
    @NotNull(message = "Trường original_link {not.null.message}")
    @NotBlank(message = "Trường original_link {blank.message}")
    private String originalLink;

    @JsonProperty("world_context_id")
    @Positive(message = "Trường world_context_id {positive.message}")
    @NotNull(message = "Trường world_context_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường world_context_id {digits.message}")
    private Integer worldContextId;

    @JsonProperty("sect_id")
    @Positive(message = "Trường sect_id {positive.message}")
    @NotNull(message = "Trường sect_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường sect_id {digits.message}")
    private Integer sectId;

    @JsonProperty("category_id")
    @Positive(message = "Trường category_id {positive.message}")
    @NotNull(message = "Trường category_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường category_id {digits.message}")
    private Integer categoryId;

    @JsonProperty("original_author_name")
    @NotNull(message = "Trường original_author_name {not.null.message}")
    @NotBlank(message = "Trường original_author_name {blank.message}")
    @Length(min = 1,max = 255,message = "Trường original_author_name {length.message}")
    private String originalAuthorName;

    @JsonProperty("author_name")
    @NotNull(message = "Trường authorName {not.null.message}")
    @NotBlank(message = "Trường authorName {blank.message}")
    @Length(min = 1,max = 255,message = "Trường authorName {length.message}")
    private String authorName;
}
