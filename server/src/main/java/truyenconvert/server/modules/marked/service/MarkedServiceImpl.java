package truyenconvert.server.modules.marked.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;

@Service
public class MarkedServiceImpl implements MarkedService{
    @Override
    public ResponseSuccess<Boolean> saveMarkedBook(int bookId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> unSaveMarkedBook(int id, User user) {
        return null;
    }
}
