package truyenconvert.server.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews", indexes = {
        @Index(name = "idx_reviews_user_id", columnList = "user_id"),
        @Index(name = "idx_reviews_book_id", columnList = "book_id")
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 1000)
    private String content;

    @Column(nullable = false)
    @ColumnDefault(value = "'5'")
    private float score;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;


    //User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    // Story
    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    @JsonBackReference
    private Book book;
}
