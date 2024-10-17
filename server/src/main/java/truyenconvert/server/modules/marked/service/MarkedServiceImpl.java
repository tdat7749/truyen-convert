package truyenconvert.server.modules.marked.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.marked.repositories.MarkedRepository;
import truyenconvert.server.modules.notification.service.NotificationService;

@Service
public class MarkedServiceImpl implements MarkedService{
    private final MarkedRepository markedRepository;
    private final MessageService messageService;
    private final NotificationService notificationService;

    public MarkedServiceImpl(
            MarkedRepository markedRepository,
            MessageService messageService,
            NotificationService notificationService
    ) {
        this.markedRepository = markedRepository;
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @Override
    public ResponseSuccess<Boolean> saveMarkedBook(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> unSaveMarkedBook(int id, User user) {
        return null;
    }
}
