package truyenconvert.server.modules.book.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("original_author_name")
    private String originalAuthorName;
}
