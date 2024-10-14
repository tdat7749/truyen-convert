package truyenconvert.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapters", indexes = {
        @Index(name = "idx_book_id", columnList = "book_id")
})
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int chapter;

    @Column(nullable = false,name = "unlock_coin")
    private long unLockCoin;

    @Column(nullable = false,name = "word_count")
    private long wordCount;

    @Column(nullable = false,name = "view_count")
    @ColumnDefault(value = "'0'")
    private long viewCount;

    @Column(name = "time_expired")
    private LocalDateTime timeExpired;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;

    // Story
    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    @JsonBackReference
    private Book book;

    // Unlock Chapter
    @ManyToMany(mappedBy = "chapters")
    private List<User> userUnlock;
}
