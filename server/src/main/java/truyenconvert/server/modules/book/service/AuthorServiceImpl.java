package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateAuthorDTO;
import truyenconvert.server.modules.book.dtos.EditAuthorDTO;
import truyenconvert.server.modules.book.vm.AuthorVm;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{
    @Override
    public Boolean createAuthor(CreateAuthorDTO dto) {
        return null;
    }

    @Override
    public Boolean editAuthor(EditAuthorDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<AuthorVm>> getAllAuthor(int pageIndex, String sortBy, String keyword) {
        return null;
    }

    @Override
    public ResponseSuccess<AuthorVm> getAuthorById(int id) {
        return null;
    }
}
