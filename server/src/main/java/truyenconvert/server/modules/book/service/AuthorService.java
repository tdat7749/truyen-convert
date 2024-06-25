package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateAuthorDTO;
import truyenconvert.server.modules.book.dtos.EditAuthorDTO;
import truyenconvert.server.modules.book.vm.AuthorVm;

import java.util.List;

@Service
public interface AuthorService {
    Boolean createAuthor(CreateAuthorDTO dto);
    Boolean editAuthor(EditAuthorDTO dto, User user);
    ResponseSuccess<List<AuthorVm>> getAllAuthor(int pageIndex, String sortBy, String keyword);
    ResponseSuccess<AuthorVm> getAuthorById(int id);
}
