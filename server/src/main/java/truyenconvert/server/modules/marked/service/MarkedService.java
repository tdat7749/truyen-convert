package truyenconvert.server.modules.marked.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;

@Service
public interface MarkedService {
    ResponseSuccess<Boolean> saveMarkedBook(int bookId, User user);
    ResponseSuccess<Boolean> unSaveMarkedBook(int id, User user);
}
