package truyenconvert.server.modules.book.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authors",indexes = {
        @Index(name = "idx_author_name",columnList = "author_name"),
        @Index(name = "idx_original_author_name",columnList = "original_author_name")
})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,name = "author_name",unique = true)
    private String authorName;

    @Column(nullable = false,name = "original_author_name",unique = true)
    private String originalAuthorName;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "author")
    @JsonManagedReference
    private List<Book> books;

}
