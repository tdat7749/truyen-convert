package truyenconvert.server.modules.book.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Author;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.auth.service.AuthServiceImpl;
import truyenconvert.server.modules.book.dtos.CreateAuthorDTO;
import truyenconvert.server.modules.book.dtos.EditAuthorDTO;
import truyenconvert.server.modules.book.exceptions.AuthorNotFoundException;
import truyenconvert.server.modules.book.repositories.AuthorRepository;
import truyenconvert.server.modules.book.vm.AuthorVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Value("${truyencv.page-size}")
    private int pageSize;

    private final AuthorRepository authorRepository;
    private final MessageService messageService;
    private final MappingService mappingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImpl.class);

    public AuthorServiceImpl(
            AuthorRepository authorRepository,
            MessageService messageService,
            MappingService mappingService
    ){
        this.authorRepository = authorRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
    }

    @Override
    public Author createAuthor(String authorName,String originalAuthorName) {
        var authorFound = authorRepository.findByAuthorNameOrOriginalAuthorName(authorName,originalAuthorName).orElse(null);
        if(authorFound == null){
            Author author = Author.builder()
                    .authorName(authorName)
                    .originalAuthorName(originalAuthorName)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            LOGGER.info("Tác giả có tên {} vừa được tạo",authorName);
            return authorRepository.save(author);
        }

        return authorFound;
    }

    @Override
    public ResponseSuccess<Boolean> editAuthor(EditAuthorDTO dto,int id, User user) {
        var authorFound = this.findById(id).orElse(null);
        if(authorFound == null){
            throw new AuthorNotFoundException(messageService.getMessage("author.not-found"));
        }

        authorFound.setAuthorName(dto.getAuthorName());
        authorFound.setOriginalAuthorName(dto.getOriginalAuthorName());
        authorFound.setUpdatedAt(LocalDateTime.now());

        authorRepository.save(authorFound);
        LOGGER.info("{} {} chỉnh sửa tác giả {} - {}",user.getRole().name(),user.getEmail(),authorFound.getAuthorName(),authorFound.getOriginalAuthorName());
        return new ResponseSuccess<>(messageService.getMessage("author.edit.success"), true);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<AuthorVm>>> getAllAuthor(int pageIndex, String sort, String keyword) {
        var sortBy = Sort.by(Sort.Direction.DESC,"createdAt");
        if (sort.equals("a-z")){
            sortBy = Sort.by(Sort.Direction.ASC,"authorName");
        }else if(sort.equals("z-a")){
            sortBy = Sort.by(Sort.Direction.DESC,"authorName");
        }
        Pageable paging = PageRequest.of(pageIndex,pageSize, sortBy);

        var pagingResult = authorRepository.getAllAuthor(keyword,paging);

        List<AuthorVm> authorVmList = pagingResult.stream().map(mappingService::getAuthorVm).toList();

        ResponsePaging<List<AuthorVm>> result = ResponsePaging.<List<AuthorVm>>builder()
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .data(authorVmList)
                .build();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    public ResponseSuccess<AuthorVm> getAuthorById(int id) {
        var authorFound = this.findById(id).orElse(null);
        if(authorFound == null){
            throw new AuthorNotFoundException(messageService.getMessage("author.not-found"));
        }

        AuthorVm authorVm = mappingService.getAuthorVm(authorFound);

        return new ResponseSuccess<>("Thành công.",authorVm);
    }

    @Override
    public Optional<Author> findById(int id) {
        return authorRepository.findById(id);
    }
}
