package truyenconvert.server.modules.book.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorVm {
    private int id;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("original_author_name")
    private String originalAuthorName;
}
