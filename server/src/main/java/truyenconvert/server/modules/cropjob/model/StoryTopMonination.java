package truyenconvert.server.modules.cropjob.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "story_top_moninations")
public class StoryTopMonination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false,name = "category_title")
    private String categoryTitle;

    @Column(nullable = false)
    private int moninations;

    @Column(nullable = false,name = "author_name")
    private String authorName;

    @Column(nullable = false,name = "poster_name")
    private String posterName;
}
