package truyenconvert.server.modules.notification.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Notification;
import truyenconvert.server.models.NotificationUser;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.notification.dtos.CreateNotiDTO;
import truyenconvert.server.modules.notification.repository.NotificationRepository;
import truyenconvert.server.modules.notification.vm.NotificationVm;
import truyenconvert.server.modules.redis.service.RedisService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final String USER_DESTINATION = "/queue/notification";
    private final String ALL_USER_DESTINATION = "/topic/notification";
    private final String CACHE_VALUE = "notifications";
    private final int NOTIFICATION_PAGE_SIZE = 10;
    private final String NOTIFICATION_SORT_BY = "createdAt";
    private final SimpMessagingTemplate template;
    private final NotificationRepository notificationRepository;
    private final RedisService redisService;

    public NotificationServiceImpl(
            SimpMessagingTemplate template,
            NotificationRepository notificationRepository,
            RedisService redisService
    ){
        this.template = template;
        this.notificationRepository = notificationRepository;
        this.redisService = redisService;
    }

    @Override
    public void sendNotificationToUsers(Notification notification, List<User> users) {

        for(User user : users){
            template.convertAndSendToUser(user.getUsername(),USER_DESTINATION,notification.getMessage());
        }
    }

    @Override
    public void sendNotificationToUsers(Notification notification, Set<User> users) {

        for(User user : users){
            template.convertAndSendToUser(user.getUsername(),USER_DESTINATION,notification.getMessage());
        }
    }

    @Override
    public void sendNotificationToAllUsers(Notification notification) {

        template.convertAndSend(ALL_USER_DESTINATION,notification.getMessage());
    }

    @Override
    @Transactional
    public Notification createNotification(CreateNotiDTO dto, User sender, Set<User> receipters) {
        Set<NotificationUser> notificationUsers = receipters.stream()
                .map(user -> {
                    redisService.evictCachePrefixAndSuffixUserId(CACHE_VALUE,user.getId());
                    return NotificationUser.builder()
                            .isRead(false)
                            .user(user)
                            .build();
                })
                .collect(Collectors.toSet());

        Notification notification = Notification.builder()
                .createdAt(LocalDateTime.now())
                .type(dto.getType())
                .message(dto.getMessage())
                .sender(sender)
                .notificationUsers(notificationUsers)
                .build();

        notificationUsers.forEach(notiUser -> notiUser.setNotification(notification));
        return notificationRepository.save(notification);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'pageIndex:' + #pageIndex + ',userId:' + #user.id")
    public ResponseSuccess<ResponsePaging<List<NotificationVm>>> getAllNotification(int pageIndex, User user) {
        Pageable paging = PageRequest.of(pageIndex,NOTIFICATION_PAGE_SIZE, Sort.by(Sort.Direction.DESC,NOTIFICATION_SORT_BY));

        Page<NotificationVm> pagingResult = notificationRepository.getAllNotificationsForUser(paging,user);

        ResponsePaging<List<NotificationVm>> result = ResponsePaging.<List<NotificationVm>>builder()
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .pageSize(NOTIFICATION_PAGE_SIZE)
                .pageIndex(pageIndex)
                .data(pagingResult.getContent())
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

}
