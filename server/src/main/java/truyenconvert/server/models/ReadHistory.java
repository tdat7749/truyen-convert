package truyenconvert.server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "read_histories")
public class ReadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int chapter;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;


    // User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;
}
