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

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments",indexes = {
        @Index(name = "idx_parent_id",columnList = "parent_id")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    @Column(nullable = false,length = 1000)
    private String content;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;

    // User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    // Story
    @ManyToOne
    @JoinColumn(name = "story_id",nullable = false)
    @JsonBackReference
    private Story story;

    //Like Commnet
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "likesComment")
    private List<User> likes;
}
