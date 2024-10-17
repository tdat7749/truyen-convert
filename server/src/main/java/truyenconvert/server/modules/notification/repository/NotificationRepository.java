package truyenconvert.server.modules.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import truyenconvert.server.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
