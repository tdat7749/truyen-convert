package truyenconvert.server.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private long coin;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    // Giver
    @ManyToOne
    @JoinColumn(name = "giver_id")
    @JsonBackReference
    private User userGave;

    // story
    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    // Received
    @ManyToOne
    @JoinColumn(name = "poster_id")
    @JsonBackReference
    private User userReceived;
}
