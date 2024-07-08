package truyenconvert.server.modules.book.dtos;

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
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDTO {

    @NotNull(message = "Trường title {not.null.message}")
    @NotBlank(message = "Trường title {blank.message}")
    @Length(min = 1,max = 400,message = "Trường title {length.message}")
    private String title;

    @NotNull(message = "Trường slug {not.null.message}")
    @NotBlank(message = "Trường slug {blank.message}")
    @Length(min = 1,max = 430,message = "Trường slug {length.message}")
    private String slug;

    @NotNull(message = "Trường thumbnail {not.null.message}")
    @NotBlank(message = "Trường thumbnail {blank.message}")
    private String thumbnail;

    @NotNull(message = "Trường introduction {not.null.message}")
    @NotBlank(message = "Trường introduction {blank.message}")
    private String introduction;

    @JsonProperty("original_link")
    @NotNull(message = "Trường original_link {not.null.message}")
    @NotBlank(message = "Trường original_link {blank.message}")
    private String originalLink;

    @JsonProperty("origianl_name")
    @NotNull(message = "Trường origianl_name {not.null.message}")
    @NotBlank(message = "Trường origianl_name {blank.message}")
    @Length(min = 1,max = 255,message = "Trường origianl_name {length.message}")
    private String originalName;

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

    @JsonProperty("world_context_id")
    @Positive(message = "Trường world_context_id {positive.message}")
    @NotNull(message = "Trường world_context_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường world_context_id {digits.message}")
    private int worldContextId;

    @JsonProperty("sect_id")
    @Positive(message = "Trường sect_id {positive.message}")
    @NotNull(message = "Trường sect_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường sect_id {digits.message}")
    private int sectId;

    @JsonProperty("category_id")
    @Positive(message = "Trường category_id {positive.message}")
    @NotNull(message = "Trường category_id {not.null.message}")
    @Digits(integer = 9, fraction = 0, message = "Trường category_id {digits.message}")
    private int categoryId;


    public String getSlug(){
        return this.slug.trim().toLowerCase();
    }

    public String getTitle(){
        return this.title.trim();
    }

    public String getOriginalName(){
        return this.originalName.trim();
    }

    public String getOriginalLink(){
        return this.originalLink.trim().toLowerCase();
    }
}
