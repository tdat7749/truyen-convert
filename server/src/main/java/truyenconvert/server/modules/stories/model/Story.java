package truyenconvert.server.modules.stories.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import truyenconvert.server.modules.classifies.model.Category;
import truyenconvert.server.modules.classifies.model.Sect;
import truyenconvert.server.modules.classifies.model.WorldContext;
import truyenconvert.server.modules.read_histories.model.ReadHistory;
import truyenconvert.server.modules.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stories",indexes = {
        @Index(name = "idx_slug",columnList = "slug"),
        @Index(name = "idx_is_vip",columnList = "is_vip"),
        @Index(name = "idx_story_state",columnList = "state"),
        @Index(name = "idx_story_status",columnList = "status"),
        @Index(name = "idx_story_deleted",columnList = "is_deleted")
})
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 400)
    private String title;

    @Column(nullable = false,length = 430,unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String thumbnail;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String introduction;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private long view;


    @Column(nullable = false,name = "original_name")
    private String originalName;


    @Column(nullable = false,name = "original_link")
    private String originalLink;

    @Column(nullable = false,name = "is_vip")
    @ColumnDefault(value = "false")
    private boolean isVip;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false,name = "is_deleted")
    @ColumnDefault(value = "'false'")
    private boolean isDeleted;

    @Column(nullable= false)
    @ColumnDefault(value = "'0'")
    private StoryStatus status;

    @Column(nullable= false)
    @ColumnDefault(value = "'0'")
    private StoryState state;

    @Column(nullable = false,name = "new_chap_at")
    private LocalDateTime newChapAt;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;


    // Read History

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<ReadHistory> readHistories;


    // User
    @ManyToOne
    @JoinColumn(name = "poster_id",nullable = false)
    @JsonBackReference
    private User user;

    // Chapter
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Chapter> chapters;

    // Donation

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Donation> donations;

    //Comment
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Comment> comments;

    //Evaluation
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Evaluation> evaluations;

    //Nomination

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Nomination> nominations;

    //Marked
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "story")
    @JsonManagedReference
    private List<Marked> markeds;

    //Category
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    @JsonBackReference
    private Category category;

    //World Context
    @ManyToOne
    @JoinColumn(name = "world_context_id",nullable = false)
    @JsonBackReference
    private WorldContext worldContext;

    //World Context
    @ManyToOne
    @JoinColumn(name = "sect_id",nullable = false)
    @JsonBackReference
    private Sect sect;

    // Author
    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    @JsonBackReference
    private Author author;
}
