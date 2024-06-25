package truyenconvert.server.modules.comment.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.comment.dtos.CreateCommentDTO;
import truyenconvert.server.modules.comment.dtos.EditCommentDTO;
import truyenconvert.server.modules.comment.vm.CommentVm;

import java.util.List;

@Service
public interface CommentService {
    ResponseSuccess<CommentVm> createComment(CreateCommentDTO dto, User user);
    ResponseSuccess<CommentVm> editComment(EditCommentDTO dto, User user);
    ResponseSuccess<Boolean> deleteComment(int id, User user);
    ResponseSuccess<List<CommentVm>> getAllComments(int pageIndex,String slug);
}
