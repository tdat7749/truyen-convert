package truyenconvert.server.modules.comment.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.comment.dtos.CreateCommentDTO;
import truyenconvert.server.modules.comment.dtos.EditCommentDTO;
import truyenconvert.server.modules.comment.vm.CommentVm;

import java.util.List;

@Service
public interface CommentService {
    ResponseSuccess<CommentVm> createComment(CreateCommentDTO dto, User user);
    ResponseSuccess<CommentVm> editComment(EditCommentDTO dto, int id, User user);
    ResponseSuccess<Boolean> deleteComment(int id, User user);
    ResponseSuccess<ResponsePaging<List<CommentVm>>> getAllComments(int pageIndex, String slug, User user);
    ResponseSuccess<Boolean> likeComment(int id, User user);
    ResponseSuccess<Boolean> unlikeComment(int id, User user);
    ResponseSuccess<List<CommentVm>> getAllReplyComment(int id,User user);
}
