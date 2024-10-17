package truyenconvert.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books",indexes = {
        @Index(name = "idx_slug",columnList = "slug"),
        @Index(name = "idx_is_vip",columnList = "is_vip"),
        @Index(name = "idx_story_state",columnList = "state"),
        @Index(name = "idx_story_status",columnList = "status"),
        @Index(name = "idx_story_deleted",columnList = "is_deleted"),
        @Index(name = "idx_story_originName",columnList = "original_name"),
        @Index(name = "idx_story_newChapAt",columnList = "new_chap_at"),
        @Index(name = "idx_books_poster_id", columnList = "poster_id"),
        @Index(name = "idx_books_author_id", columnList = "author_id"),
        @Index(name = "idx_books_sect_id", columnList = "sect_id"),
        @Index(name = "idx_books_world_context_id", columnList = "world_context_id"),
        @Index(name = "idx_books_category_id", columnList = "category_id")
})
public class Book {
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
    @ColumnDefault(value = "'0'")
    private long view;

    @Column(nullable = false,name = "count_word")
    @ColumnDefault(value = "'0'")
    private long countWord;

    @Column(nullable = false,name = "count_comment")
    @ColumnDefault(value = "'0'")
    private long countComment;

    @Column(nullable = false,name = "count_evaluation")
    @ColumnDefault(value = "'0'")
    private long countEvaluation;

    @Column(nullable = false,name = "count_chapter")
    @ColumnDefault(value = "'0'")
    private int countChapter;


    @Column(nullable = false,name = "original_name")
    private String originalName;


    @Column(nullable = false,name = "original_link")
    private String originalLink;

    @Column(nullable = false,name = "is_vip")
    @ColumnDefault(value = "'false'")
    private boolean isVip;

    @Column(nullable = false)
    @ColumnDefault(value = "'5'")
    private float score;

    @Column(nullable = false,name = "is_deleted")
    @ColumnDefault(value = "'false'")
    private boolean isDeleted;

    @Column(nullable= false)
    @ColumnDefault(value = "'0'")
    private BookStatus status;

    @Column(nullable= false)
    @ColumnDefault(value = "'0'")
    private BookState state;

    @Column(nullable = false,name = "new_chap_at")
    private LocalDateTime newChapAt;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;


    // Read History

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<ReadHistory> readHistories;


    // User
    @ManyToOne
    @JoinColumn(name = "poster_id",nullable = false)
    @JsonBackReference
    private User user;

    // Chapter
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<Chapter> chapters;

    // Donation

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<Donation> donations;

    //Comment
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<Comment> comments;

    //Evaluation
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<Review> reviews;

    //Nomination

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    @JsonManagedReference
    private List<Nomination> nominations;

    //Marked
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
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
