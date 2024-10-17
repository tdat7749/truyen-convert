package truyenconvert.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import truyenconvert.server.models.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "notifications")
    private Set<User> users;

    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "sender_id")
    private User sender;
}
