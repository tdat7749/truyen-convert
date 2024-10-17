package truyenconvert.server.modules.notification.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Notification;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.notification.dtos.CreateNotiDTO;
import truyenconvert.server.modules.notification.vm.NotificationVm;

import java.util.List;
import java.util.Set;

@Service
public interface NotificationService {
    void sendNotificationToUsers(Notification notification, List<User> users);
    void sendNotificationToUsers(Notification notification, Set<User> users);

    void sendNotificationToAllUsers(Notification notification);

    Notification createNotification(CreateNotiDTO dto, User sender, Set<User> receipters);
    ResponseSuccess<ResponsePaging<List<NotificationVm>>> getAllNotification(int pageIndex, User user);
}
