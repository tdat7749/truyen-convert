package truyenconvert.server.modules.comment.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.comment.dtos.CreateCommentDTO;
import truyenconvert.server.modules.comment.dtos.EditCommentDTO;
import truyenconvert.server.modules.comment.vm.CommentVm;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Override
    public ResponseSuccess<CommentVm> createComment(CreateCommentDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<CommentVm> editComment(EditCommentDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteComment(int id, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<CommentVm>> getAllComments(int pageIndex, String slug) {
        return null;
    }
}
