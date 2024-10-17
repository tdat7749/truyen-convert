package truyenconvert.server.modules.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Notification;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.notification.vm.NotificationVm;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT new truyenconvert.server.modules.notification.vm.NotificationVm(n.id,n.message,n.type,cast(n.createdAt as String),n.sender,nt.isRead,cast(nt.timeRead as String)) FROM Notification AS n INNER JOIN n.notificationUsers AS nt WHERE nt.user =:user")
    Page<NotificationVm> getAllNotificationsForUser(Pageable paging, User user);
}
