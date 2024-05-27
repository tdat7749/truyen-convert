package truyenconvert.server.modules.stories.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import truyenconvert.server.modules.users.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nomination")
public class Nomination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;


    //User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    // Story
    @ManyToOne
    @JoinColumn(name = "story_id",nullable = false)
    @JsonBackReference
    private Story story;

}
