package truyenconvert.server.modules.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import truyenconvert.server.models.Notification;
import truyenconvert.server.models.User;

import java.util.List;
import java.util.Set;

public class NotificationServiceImpl implements NotificationService{

    private final SimpMessagingTemplate template;

    public NotificationServiceImpl(
            SimpMessagingTemplate template
    ){
        this.template = template;
    }

    @Override
    public void sendNotificationToUsers(Notification notification, List<User> users) {

        for(User user : users){
            template.convertAndSendToUser(user.getUsername(),"/queue/notification",notification.getMessage());
        }
    }

    @Override
    public void sendNotificationToUsers(Notification notification, Set<User> users) {

        for(User user : users){
            template.convertAndSendToUser(user.getUsername(),"/queue/notification",notification.getMessage());
        }
    }

    @Override
    public void sendNotificationToAllUsers(Notification notification) {

        template.convertAndSend("/topic/notification",notification.getMessage());
    }
}
