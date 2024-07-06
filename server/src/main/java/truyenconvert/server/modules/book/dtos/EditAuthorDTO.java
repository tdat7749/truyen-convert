package truyenconvert.server.modules.book.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditAuthorDTO {
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("original_author_name")
    private String originalAuthorName;
}
