package truyenconvert.server.modules.notification.service;

import truyenconvert.server.models.Notification;
import truyenconvert.server.models.User;

import java.util.List;
import java.util.Set;

public interface NotificationService {
    void sendNotificationToUsers(Notification notification, List<User> users);
    void sendNotificationToUsers(Notification notification, Set<User> users);

    void sendNotificationToAllUsers(Notification notification);
}
