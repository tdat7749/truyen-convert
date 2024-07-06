package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Author;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateAuthorDTO;
import truyenconvert.server.modules.book.dtos.EditAuthorDTO;
import truyenconvert.server.modules.book.vm.AuthorVm;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthorService {
    Author createAuthor(String authorName,String originalAuthorName);
    ResponseSuccess<Boolean> editAuthor(EditAuthorDTO dto,int id, User user);
    ResponseSuccess<ResponsePaging<List<AuthorVm>>> getAllAuthor(int pageIndex, String sort, String keyword);
    ResponseSuccess<AuthorVm> getAuthorById(int id);
    Optional<Author> findById(int id);
}
