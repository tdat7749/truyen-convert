package truyenconvert.server.modules.comment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Comment;
import truyenconvert.server.models.User;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.modules.book.exceptions.BookNotFoundException;
import truyenconvert.server.modules.book.service.BookService;
import truyenconvert.server.modules.book.service.BookServiceImpl;
import truyenconvert.server.modules.comment.dtos.CreateCommentDTO;
import truyenconvert.server.modules.comment.dtos.EditCommentDTO;
import truyenconvert.server.modules.comment.exceptions.CommentNotFoundException;
import truyenconvert.server.modules.comment.exceptions.NotCreaterOfCommentException;
import truyenconvert.server.modules.comment.exceptions.UserLikedCommentException;
import truyenconvert.server.modules.comment.repositories.CommentRepository;
import truyenconvert.server.modules.comment.vm.CommentVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final BookService bookService;
    private final MessageService messageService;
    private final MappingService mappingService;

    private final UserService userService;
    private final CommentRepository commentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final String CACHE_VALUE = "comments";

    // comment page size
    private final int PAGE_SIZE = 10;

    public CommentServiceImpl(
        BookService bookService,
        MessageService messageService,
        MappingService mappingService,
        UserService userService,
        CommentRepository commentRepository
    ){
        this.bookService = bookService;
        this.messageService = messageService;
        this.mappingService = mappingService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<CommentVm> createComment(CreateCommentDTO dto, User user) {
        var bookFound = this.bookService.findById(dto.getBookId()).orElse(null);
        if(bookFound == null){
            LOGGER.error(messageService.getMessage("book.log.not-found"),dto.getBookId());
            throw new BookNotFoundException(messageService.getMessage("book.not-found"));
        }

        Comment parent = null;

        if(dto.getParentId() != null){
            parent = commentRepository.findById(dto.getParentId()).orElse(null);
            if(parent == null){
                LOGGER.error(messageService.getMessage("comment.log.not-found"), dto.getParentId());
                throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
            }
        }

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .book(bookFound)
                .parent(parent)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var save = commentRepository.save(comment);
        var commentVm = mappingService.getCommentVm(save);
        commentVm.setLiked(false);
        commentVm.setTotalLike(0);

        LOGGER.info(messageService.getMessage("comment.log.created.success"),user.getId(),save.getId());

        return new ResponseSuccess<>(messageService.getMessage("comment.created.success"),commentVm);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<CommentVm> editComment(EditCommentDTO dto, int id, User user) {
        var commentFound = commentRepository.findById(id).orElse(null);
        if(commentFound == null){
            LOGGER.error(messageService.getMessage("comment.log.not-found"), id);
            throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
        }

        commentFound.setContent(dto.getContent());
        commentFound.setUpdatedAt(LocalDateTime.now());

        var save = commentRepository.save(commentFound);

        var commentVm = mappingService.getCommentVm(save);

        LOGGER.info(messageService.getMessage("comment.log.edit.success"),user.getId(),commentFound.getId());
        return new ResponseSuccess<>(messageService.getMessage("comment.edit.success"),commentVm);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteComment(int id, User user) {
        var commentFound = commentRepository.findById(id).orElse(null);
        if(commentFound == null){
            LOGGER.error(messageService.getMessage("comment.log.not-found"), id);
            throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
        }

        var authorOfComment = commentFound.getUser();
        if(!authorOfComment.getRole().equals(Role.MODERATOR) && !authorOfComment.getRole().equals(Role.ADMIN)){
            if(!authorOfComment.equals(user)){
                LOGGER.error(messageService.getMessage("comment.log.not-creater"), user.getId(), id);
                throw new NotCreaterOfCommentException(messageService.getMessage("comment.not-creater"));
            }
        }

        commentRepository.delete(commentFound);

        LOGGER.info(messageService.getMessage("comment.log.deleted.success"),user.getId(),commentFound.getId());
        return new ResponseSuccess<>(messageService.getMessage("comment.deleted.success"),true);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'pageIndex:' + #pageIndex + ',slug:' + #slug + ',user_id:' + (#user != null ? #user.id : 'null')")
    public ResponseSuccess<ResponsePaging<List<CommentVm>>> getAllComments(int pageIndex, String slug,User user) {
        Pageable paging = PageRequest.of(pageIndex,PAGE_SIZE, Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<CommentVm> pagingResult = commentRepository.getAllComments(paging, slug, user);

        ResponsePaging<List<CommentVm>> responsePaging = ResponsePaging.<List<CommentVm>>builder()
                .totalPage(pagingResult.getTotalPages())
                .totalRecord(pagingResult.getTotalElements())
                .pageSize(PAGE_SIZE)
                .pageIndex(pageIndex)
                .data(pagingResult.getContent())
                .build();

        return new ResponseSuccess<>("Thành công.",responsePaging);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> likeComment(int id, User user) {
        var commentFound = commentRepository.findById(id).orElse(null);
        if(commentFound == null){
            LOGGER.error(messageService.getMessage("comment.log.not-found"), id);
            throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
        }

        boolean isLiked = commentRepository.getIsLiked(user.getId(),commentFound.getId());
        if(isLiked){
            LOGGER.error(messageService.getMessage("comment.log.user-liked"), user.getId(), id);
            throw new UserLikedCommentException(messageService.getMessage("comment.user-liked"));
        }

        var userFound = userService.findById(user.getId()).orElse(null);
        if(userFound == null){
            LOGGER.error(messageService.getMessage("user.log.not-found"), user.getId());
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }
        commentFound.getLikes().add(userFound);
        userFound.getLikesComment().add(commentFound);

        userService.save(userFound);
        commentRepository.save(commentFound);

        LOGGER.info(messageService.getMessage("comment.log.liked.success"), user.getId(), id);
        return new ResponseSuccess<>(messageService.getMessage("comment.liked.success"),true);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> unlikeComment(int id, User user) {
        var commentFound = commentRepository.findById(id).orElse(null);
        if(commentFound == null){
            LOGGER.error(messageService.getMessage("comment.log.not-found"), id);
            throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
        }

        boolean isLiked = commentRepository.getIsLiked(user.getId(),commentFound.getId());
        if(!isLiked){
            LOGGER.error(messageService.getMessage("comment.log.has-not-liked"), user.getId(), id);
            throw new UserLikedCommentException(messageService.getMessage("comment.has-not-liked"));
        }

        var userFound = userService.findById(user.getId()).orElse(null);
        if(userFound == null){
            LOGGER.error(messageService.getMessage("user.log.not-found"), user.getId());
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }
        commentFound.getLikes().remove(userFound);
        userFound.getLikesComment().remove(commentFound);

        userService.save(userFound);
        commentRepository.save(commentFound);

        LOGGER.info(messageService.getMessage("comment.log.liked.success"), user.getId(), id);
        return new ResponseSuccess<>(messageService.getMessage("comment.liked.success"),true);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "'id:' + #id + ',user_id:' + (#user != null ? #user.id : 'null')")
    public ResponseSuccess<List<CommentVm>> getAllReplyComment(int id, User user) {
        var commentFound = commentRepository.findById(id).orElse(null);
        if(commentFound == null){
            LOGGER.error(messageService.getMessage("comment.log.not-found"), id);
            throw new CommentNotFoundException(messageService.getMessage("comment.not-found"));
        }

        var result = commentRepository.getAllReplyComment(commentFound,user);

        return new ResponseSuccess<>("Thành công.",result);
    }
}
