package truyenconvert.server.modules.marked.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.marked.repositories.MarkedRepository;

@Service
public class MarkedServiceImpl implements MarkedService{
    private final MarkedRepository markedRepository;
    private final MessageService messageService;

    public MarkedServiceImpl(
            MarkedRepository markedRepository,
            MessageService messageService
    ) {
        this.markedRepository = markedRepository;
        this.messageService = messageService;
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
