package truyenconvert.server.modules.stories.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import truyenconvert.server.modules.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapters")
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

    @Column(name = "time_expired")
    private LocalDateTime timeExpired;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;

    // Story
    @ManyToOne
    @JoinColumn(name = "story_id",nullable = false)
    @JsonBackReference
    private Story story;

    // Unlock Chapter
    @ManyToMany(mappedBy = "chapters")
    private List<User> userUnlock;
}
