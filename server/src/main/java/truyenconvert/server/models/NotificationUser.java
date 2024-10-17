package truyenconvert.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import truyenconvert.server.modules.notification.vm.NotificationVm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications_users", indexes = {
    @Index(name = "idx_notifications_users_user_id", columnList = "user_id"),
    @Index(name = "idx_notifications_users_notification_id", columnList = "notification_id")
})
public class NotificationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,name = "is_read")
    @ColumnDefault(value = "'false'")
    private boolean isRead;

    @Column(name = "time_read")
    private LocalDateTime timeRead;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id",nullable = false)
    @JsonBackReference
    private Notification notification;
}
